package uk.ac.ncl.csc8019backend.business.raildata.controller;

import uk.ac.ncl.csc8019backend.business.raildata.entity.TrainService;
import uk.ac.ncl.csc8019backend.business.raildata.service.TrainDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/raildata")
@CrossOrigin(origins = "*")
public class RailDataController {

    @Autowired
    private TrainDataService trainDataService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "RailData Service");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/trains")
    public ResponseEntity<List<TrainService>> getAllTrains() {
        List<TrainService> trains = trainDataService.getAllActiveTrains();
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/trains/{id}")
    public ResponseEntity<TrainService> getTrainById(@PathVariable Long id) {
        Optional<TrainService> train = trainDataService.getTrainById(id);
        return train.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/trains/trainId/{trainId}")
    public ResponseEntity<TrainService> getTrainByTrainId(@PathVariable String trainId) {
        Optional<TrainService> train = trainDataService.getTrainByTrainId(trainId);
        return train.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/trains/upcoming")
    public ResponseEntity<List<TrainService>> getUpcomingTrains(
            @RequestParam(defaultValue = "30") int minutes) {
        List<TrainService> trains = trainDataService.getUpcomingTrainsToCramlington(minutes);
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/trains/delayed")
    public ResponseEntity<List<TrainService>> getDelayedTrains() {
        List<TrainService> trains = trainDataService.getDelayedTrains();
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/trains/byOrigin")
    public ResponseEntity<List<TrainService>> getTrainsByOrigin(
            @RequestParam String station) {
        List<TrainService> trains = trainDataService.getTrainsByOrigin(station);
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/trains/byDestination")
    public ResponseEntity<List<TrainService>> getTrainsByDestination(
            @RequestParam String station) {
        List<TrainService> trains = trainDataService.getTrainsByDestination(station);
        return ResponseEntity.ok(trains);
    }

    @GetMapping("/stations")
    public ResponseEntity<List<String>> getAllStations() {
        List<String> stations = trainDataService.getAllStationNames();
        return ResponseEntity.ok(stations);
    }

    @PostMapping("/trains")
    public ResponseEntity<TrainService> addTrain(@RequestBody TrainService trainService) {
        TrainService savedTrain = trainDataService.addTrainService(trainService);
        return new ResponseEntity<>(savedTrain, HttpStatus.CREATED);
    }

    @PutMapping("/trains/{id}/status")
    public ResponseEntity<TrainService> updateTrainStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) Integer delayMinutes) {
        Optional<TrainService> updatedTrain = trainDataService.updateTrainStatus(id, status, delayMinutes);
        return updatedTrain.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/trains/{id}/arrive")
    public ResponseEntity<TrainService> markTrainAsArrived(@PathVariable Long id) {
        Optional<TrainService> train = trainDataService.markTrainAsArrived(id);
        return train.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/trains/{id}")
    public ResponseEntity<Void> cancelTrain(@PathVariable Long id) {
        Optional<TrainService> train = trainDataService.cancelTrainService(id);
        return train.map(t -> new ResponseEntity<Void>(HttpStatus.NO_CONTENT))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/trains/refresh")
    public ResponseEntity<Map<String, String>> refreshTrainData() {
        trainDataService.fetchAndUpdateTrainData();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Train data refresh initiated");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/trains/init-mock")
    public ResponseEntity<Map<String, String>> initializeMockData() {
        trainDataService.initializeMockData();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Mock train data initialized");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recommendation")
    public ResponseEntity<?> recommendTrainForOrder(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime pickupTime) {
        Optional<TrainService> recommendedTrain = trainDataService.recommendTrainForOrderTime(pickupTime);

        if (recommendedTrain.isPresent()) {
            return ResponseEntity.ok(recommendedTrain.get());
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "No suitable train found for this pickup time");
            response.put("suggestion", "Consider adjusting your pickup time");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/trains/{id}/delay-info")
    public ResponseEntity<Map<String, Object>> getTrainDelayInfo(@PathVariable Long id) {
        Optional<TrainService> optionalTrain = trainDataService.getTrainById(id);

        if (optionalTrain.isPresent()) {
            TrainService train = optionalTrain.get();
            Map<String, Object> delayInfo = new HashMap<>();
            delayInfo.put("trainId", train.getTrainId());
            delayInfo.put("isDelayed", train.isDelayed());
            delayInfo.put("delayMinutes", train.getDelayMinutes());
            delayInfo.put("status", train.getStatus());
            delayInfo.put("scheduledArrival", train.getScheduledArrivalTime());
            delayInfo.put("estimatedArrival", train.getEstimatedArrivalTime());
            delayInfo.put("actualArrival", train.getActualArrivalTime());
            delayInfo.put("lastUpdated", train.getLastUpdated());

            return ResponseEntity.ok(delayInfo);
        }

        return ResponseEntity.notFound().build();
    }
}