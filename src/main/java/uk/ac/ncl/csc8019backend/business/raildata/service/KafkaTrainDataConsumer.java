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
import java.util.Set;

@Service
public class KafkaTrainDataConsumer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTrainDataConsumer.class);

    @Autowired
    private TrainServiceRepository trainServiceRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;


    private static final Set<String> CRAMLINGTON_STATION_CODES = Set.of(
            "CRL",
            "CML",
            "74800",
            "74801"
    );

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

            String originLocation = getValue(body, "origin_location");
            String destinationLocation = getValue(body, "destination_location");

            if (originLocation.isEmpty()) {
                originLocation = getValue(body, "origin_stanox");
            }
            if (destinationLocation.isEmpty()) {
                destinationLocation = getValue(body, "destination_stanox");
            }


            boolean isCramlington = isCramlingtonStation(locStanox) ||
                    isCramlingtonStation(originLocation) ||
                    isCramlingtonStation(destinationLocation);


            if (!isCramlington) {
                logger.debug("Skipping non-Cramlington train: {} at station {}", trainId, locStanox);
                return;
            }

            logger.debug("Processing {} event for Cramlington train {}", eventType, trainId);

            LocalDateTime scheduledTime = null;
            if (!plannedTime.isEmpty()) {
                scheduledTime = parseTimestamp(plannedTime);
            }

            LocalDateTime actual = null;
            if (!actualTime.isEmpty()) {
                actual = parseTimestamp(actualTime);
            }

            Optional<TrainService> existingTrain = trainServiceRepository.findByTrainId(trainId);
            TrainService train = existingTrain.orElse(new TrainService());

            train.setTrainId(trainId);
            train.setCurrentStation(locStanox);
            train.setPlatform(platform);
            train.setLastUpdated(LocalDateTime.now());
            train.setIsActive(true);

            if (!originLocation.isEmpty()) {
                train.setOriginStation(originLocation);
            }
            if (!destinationLocation.isEmpty()) {
                train.setDestinationStation(destinationLocation);
            }

            if ("ARRIVAL".equals(eventType)) {
                if (scheduledTime != null) {
                    train.setScheduledArrivalTime(scheduledTime);
                }
                if (actual != null) {
                    train.setActualArrivalTime(actual);
                }
            } else if ("DEPARTURE".equals(eventType)) {
                if (scheduledTime != null) {
                    train.setScheduledDepartureTime(scheduledTime);
                }
                if (actual != null) {
                    train.setEstimatedDepartureTime(actual);
                }
            }

            if (actual != null) {
                LocalDateTime scheduled = "ARRIVAL".equals(eventType) ?
                        train.getScheduledArrivalTime() : train.getScheduledDepartureTime();

                if (scheduled != null) {
                    long delayMinutes = java.time.Duration.between(scheduled, actual).toMinutes();

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
            } else if (train.getStatus() == null) {
                if ("ARRIVAL".equals(eventType) || "DEPARTURE".equals(eventType)) {
                    train.setStatus("ON_TIME");
                    train.setDelayMinutes(0);
                }
            }

            TrainService saved = trainServiceRepository.save(train);
            logger.info("✅ CRAMLINGTON TRAIN: {} | Event: {} | From: {} → To: {} | Status: {} | Platform: {}",
                    trainId, eventType,
                    train.getOriginStation() != null ? train.getOriginStation() : "?",
                    train.getDestinationStation() != null ? train.getDestinationStation() : "?",
                    train.getStatus(), platform);

        } catch (Exception e) {
            logger.error("Error processing train message: {}", e.getMessage());
        }
    }


    private boolean isCramlingtonStation(String stationCode) {
        if (stationCode == null || stationCode.isEmpty()) {
            return false;
        }


        if (CRAMLINGTON_STATION_CODES.contains(stationCode)) {
            return true;
        }


        String lowerCode = stationCode.toLowerCase();
        return lowerCode.contains("cram") ||
                lowerCode.contains("cml") ||
                lowerCode.contains("crl");
    }

    private LocalDateTime parseTimestamp(String timestamp) {
        try {
            if (timestamp.matches("\\d+")) {
                long millis = Long.parseLong(timestamp);
                return LocalDateTime.ofInstant(
                        java.time.Instant.ofEpochMilli(millis),
                        java.time.ZoneOffset.UTC
                );
            } else {
                return LocalDateTime.parse(timestamp, formatter);
            }
        } catch (Exception e) {
            logger.error("Failed to parse timestamp: {}", timestamp);
            throw e;
        }
    }

    private String getValue(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isNull() ? "" : value.asText();
    }
}