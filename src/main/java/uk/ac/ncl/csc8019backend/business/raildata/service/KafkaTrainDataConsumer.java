package uk.ac.ncl.csc8019backend.business.raildata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import uk.ac.ncl.csc8019backend.business.raildata.entity.TrainService;
import uk.ac.ncl.csc8019backend.business.raildata.repository.TrainServiceRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class KafkaTrainDataConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTrainDataConsumer.class);

    @Autowired
    private TrainServiceRepository trainServiceRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public KafkaTrainDataConsumer() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @KafkaListener(topics = "TRAIN_MVT_ALL_TOC", groupId = "SC-3929aa57-f69e-4571-9712-d7f78358efd5")
    public void consumeTrainMovement(String message) {
        try {
            logger.debug("Received Kafka message: {}", message.substring(0, Math.min(message.length(), 100)));

            JsonNode root = objectMapper.readTree(message);

            if (root.isArray()) {
                logger.info("Received array of {} messages", root.size());
                for (JsonNode node : root) {
                    processTrainMessage(node);
                }
            } else {
                processTrainMessage(root);
            }
        } catch (Exception e) {
            logger.error("Failed to process Kafka message: {}", e.getMessage());
        }
    }

    private void processTrainMessage(JsonNode msg) {
        try {

            JsonNode body = msg.path("body");
            if (body.isMissingNode() || body.isNull()) {
                logger.debug("Message has no body, skipping");
                return;
            }

            String trainId = getValue(body, "train_id");
            if (trainId.isEmpty()) {
                logger.debug("train_id is empty, skipping");
                return;
            }

            String eventType = getValue(body, "event_type");
            String locStanox = getValue(body, "loc_stanox");
            String plannedTime = getValue(body, "planned_timestamp");
            String actualTime = getValue(body, "actual_timestamp");
            String platform = getValue(body, "platform");
            String variationStatus = getValue(body, "variation_status");


            if (!"ARRIVAL".equals(eventType)) {
                logger.debug("Skipping non-arrival event: {}", eventType);

            }


            Optional<TrainService> existingTrain = trainServiceRepository.findByTrainId(trainId);
            TrainService train = existingTrain.orElse(new TrainService());

            train.setTrainId(trainId);
            train.setCurrentStation(locStanox);
            train.setPlatform(platform);
            train.setLastUpdated(LocalDateTime.now());
            train.setIsActive(true);


            try {
                if (!plannedTime.isEmpty()) {

                    if (plannedTime.matches("\\d+")) {
                        long timestamp = Long.parseLong(plannedTime);
                        LocalDateTime scheduled = LocalDateTime.ofEpochSecond(timestamp/1000, 0, java.time.ZoneOffset.UTC);
                        train.setScheduledArrivalTime(scheduled);
                    } else {
                        LocalDateTime scheduled = LocalDateTime.parse(plannedTime, formatter);
                        train.setScheduledArrivalTime(scheduled);
                    }
                }

                if (!actualTime.isEmpty()) {
                    LocalDateTime actual;
                    if (actualTime.matches("\\d+")) {
                        long timestamp = Long.parseLong(actualTime);
                        actual = LocalDateTime.ofEpochSecond(timestamp/1000, 0, java.time.ZoneOffset.UTC);
                    } else {
                        actual = LocalDateTime.parse(actualTime, formatter);
                    }
                    train.setActualArrivalTime(actual);


                    if (train.getScheduledArrivalTime() != null) {
                        long delayMinutes = java.time.Duration.between(
                                train.getScheduledArrivalTime(), actual).toMinutes();

                        if (delayMinutes > 0) {
                            train.setDelayMinutes((int) delayMinutes);
                            train.setStatus("DELAYED");
                        } else if (delayMinutes < 0) {
                            train.setStatus("EARLY");
                            train.setDelayMinutes(0);
                        } else {
                            train.setStatus("ON_TIME");
                            train.setDelayMinutes(0);
                        }
                    }
                } else if ("LATE".equals(variationStatus)) {
                    train.setStatus("DELAYED");
                    train.setDelayMinutes(5);
                }
            } catch (Exception e) {
                logger.error("Time parsing error: {}", e.getMessage());
                train.setStatus("UNKNOWN");
            }


            TrainService saved = trainServiceRepository.save(train);
            logger.info("Saved train: {} at station {} - {}",
                    trainId, locStanox, train.getStatus());

        } catch (Exception e) {
            logger.error("Error processing train message: {}", e.getMessage());
        }
    }

    private String getValue(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isNull() ? "" : value.asText();
    }
}