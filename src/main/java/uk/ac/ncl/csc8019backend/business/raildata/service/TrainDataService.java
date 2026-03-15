package uk.ac.ncl.csc8019backend.business.raildata.service;

import uk.ac.ncl.csc8019backend.business.raildata.entity.TrainService;
import uk.ac.ncl.csc8019backend.business.raildata.repository.TrainServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class TrainDataService {

    @Autowired
    private TrainServiceRepository trainServiceRepository;

    private final Random random = new Random();

    @Transactional
    public void fetchAndUpdateTrainData() {
        List<TrainService> currentTrains = trainServiceRepository.findByIsActiveTrue();

        for (TrainService train : currentTrains) {
            if (random.nextInt(100) < 10) {
                int delay = random.nextInt(30) + 1;
                train.setDelayMinutes(delay);
                train.setStatus("DELAYED");
                train.setEstimatedArrivalTime(train.getScheduledArrivalTime().plusMinutes(delay));
            } else {
                train.setDelayMinutes(0);
                train.setStatus("ON_TIME");
                train.setEstimatedArrivalTime(train.getScheduledArrivalTime());
            }
            train.setLastUpdated(LocalDateTime.now());
        }

        trainServiceRepository.saveAll(currentTrains);
    }

    public List<TrainService> getAllActiveTrains() {
        return trainServiceRepository.findByIsActiveTrue();
    }

    public Optional<TrainService> getTrainById(Long id) {
        return trainServiceRepository.findById(id);
    }

    public Optional<TrainService> getTrainByTrainId(String trainId) {
        return trainServiceRepository.findByTrainId(trainId);
    }

    public List<TrainService> getUpcomingTrainsToCramlington(int minutes) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime futureTime = now.plusMinutes(minutes);
        return trainServiceRepository.findByScheduledArrivalTimeBetween(now, futureTime);
    }

    public List<TrainService> getDelayedTrains() {
        return trainServiceRepository.findDelayedTrains();
    }

    public List<TrainService> getTrainsByOrigin(String stationName) {
        return trainServiceRepository.findByOriginStationContainingAndIsActiveTrue(stationName);
    }

    public List<TrainService> getTrainsByDestination(String stationName) {
        return trainServiceRepository.findByDestinationStationContainingAndIsActiveTrue(stationName);
    }

    public List<String> getAllStationNames() {
        return trainServiceRepository.findAllStationNames();
    }

    @Transactional
    public TrainService addTrainService(TrainService trainService) {
        trainService.setIsActive(true);
        trainService.setLastUpdated(LocalDateTime.now());
        return trainServiceRepository.save(trainService);
    }

    @Transactional
    public Optional<TrainService> updateTrainStatus(Long id, String status, Integer delayMinutes) {
        Optional<TrainService> optionalTrain = trainServiceRepository.findById(id);

        if (optionalTrain.isPresent()) {
            TrainService train = optionalTrain.get();
            train.setStatus(status);

            if (delayMinutes != null && delayMinutes > 0) {
                train.setDelayMinutes(delayMinutes);
                train.setEstimatedArrivalTime(train.getScheduledArrivalTime().plusMinutes(delayMinutes));
            }

            train.setLastUpdated(LocalDateTime.now());
            return Optional.of(trainServiceRepository.save(train));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<TrainService> markTrainAsArrived(Long id) {
        Optional<TrainService> optionalTrain = trainServiceRepository.findById(id);

        if (optionalTrain.isPresent()) {
            TrainService train = optionalTrain.get();
            train.setStatus("ARRIVED");
            train.setActualArrivalTime(LocalDateTime.now());
            train.setLastUpdated(LocalDateTime.now());
            return Optional.of(trainServiceRepository.save(train));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<TrainService> cancelTrainService(Long id) {
        Optional<TrainService> optionalTrain = trainServiceRepository.findById(id);

        if (optionalTrain.isPresent()) {
            TrainService train = optionalTrain.get();
            train.setStatus("CANCELLED");
            train.setIsActive(false);
            train.setLastUpdated(LocalDateTime.now());
            return Optional.of(trainServiceRepository.save(train));
        }

        return Optional.empty();
    }

    @Transactional
    public void initializeMockData() {
        if (trainServiceRepository.count() == 0) {
            LocalDateTime now = LocalDateTime.now();

            TrainService train1 = new TrainService(
                    "NR1234", "Newcastle", "Edinburgh",
                    now.withHour(8).withMinute(15).withSecond(0).withNano(0),
                    "ON_TIME"
            );
            train1.setPlatform("1");

            TrainService train2 = new TrainService(
                    "NR5678", "Morpeth", "Newcastle",
                    now.withHour(8).withMinute(30).withSecond(0).withNano(0),
                    "ON_TIME"
            );
            train2.setPlatform("2");

            TrainService train3 = new TrainService(
                    "NR9012", "Newcastle", "Edinburgh",
                    now.withHour(9).withMinute(0).withSecond(0).withNano(0),
                    "DELAYED"
            );
            train3.setPlatform("1");
            train3.setDelayMinutes(5);
            train3.setEstimatedArrivalTime(train3.getScheduledArrivalTime().plusMinutes(5));

            trainServiceRepository.saveAll(List.of(train1, train2, train3));
        }
    }

    public Optional<TrainService> recommendTrainForOrderTime(LocalDateTime orderPickupTime) {
        LocalDateTime windowStart = orderPickupTime.minusMinutes(15);
        LocalDateTime windowEnd = orderPickupTime.plusMinutes(5);

        List<TrainService> trains = trainServiceRepository.findByScheduledArrivalTimeBetween(windowStart, windowEnd);

        if (!trains.isEmpty()) {
            return trains.stream()
                    .filter(t -> "ON_TIME".equals(t.getStatus()) || "DELAYED".equals(t.getStatus()))
                    .findFirst();
        }

        return Optional.empty();
    }
}