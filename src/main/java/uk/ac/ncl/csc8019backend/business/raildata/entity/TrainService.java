package uk.ac.ncl.csc8019backend.business.raildata.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "train_services")
public class TrainService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "train_id", unique = true)
    private String trainId;

    @Column(name = "origin_station")
    private String originStation;

    @Column(name = "destination_station")
    private String destinationStation;

    @Column(name = "current_station")
    private String currentStation;

    @Column(name = "scheduled_arrival_time")
    private LocalDateTime scheduledArrivalTime;

    @Column(name = "estimated_arrival_time")
    private LocalDateTime estimatedArrivalTime;

    @Column(name = "actual_arrival_time")
    private LocalDateTime actualArrivalTime;

    @Column(name = "scheduled_departure_time")
    private LocalDateTime scheduledDepartureTime;

    @Column(name = "estimated_departure_time")
    private LocalDateTime estimatedDepartureTime;

    @Column(name = "status")
    private String status;

    @Column(name = "delay_minutes")
    private Integer delayMinutes;

    @Column(name = "platform")
    private String platform;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "is_active")
    private Boolean isActive;

    public TrainService() {}

    public TrainService(String trainId, String originStation, String destinationStation,
                        LocalDateTime scheduledArrivalTime, String status) {
        this.trainId = trainId;
        this.originStation = originStation;
        this.destinationStation = destinationStation;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.status = status;
        this.isActive = true;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getOriginStation() {
        return originStation;
    }

    public void setOriginStation(String originStation) {
        this.originStation = originStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public void setDestinationStation(String destinationStation) {
        this.destinationStation = destinationStation;
    }

    public String getCurrentStation() {
        return currentStation;
    }

    public void setCurrentStation(String currentStation) {
        this.currentStation = currentStation;
    }

    public LocalDateTime getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public void setScheduledArrivalTime(LocalDateTime scheduledArrivalTime) {
        this.scheduledArrivalTime = scheduledArrivalTime;
    }

    public LocalDateTime getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(LocalDateTime estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public LocalDateTime getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(LocalDateTime actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public LocalDateTime getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public void setScheduledDepartureTime(LocalDateTime scheduledDepartureTime) {
        this.scheduledDepartureTime = scheduledDepartureTime;
    }

    public LocalDateTime getEstimatedDepartureTime() {
        return estimatedDepartureTime;
    }

    public void setEstimatedDepartureTime(LocalDateTime estimatedDepartureTime) {
        this.estimatedDepartureTime = estimatedDepartureTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getDelayMinutes() {
        return delayMinutes;
    }

    public void setDelayMinutes(Integer delayMinutes) {
        this.delayMinutes = delayMinutes;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isDelayed() {
        return delayMinutes != null && delayMinutes > 0;
    }

    public LocalDateTime getEffectiveArrivalTime() {
        if (actualArrivalTime != null) {
            return actualArrivalTime;
        } else if (estimatedArrivalTime != null) {
            return estimatedArrivalTime;
        } else {
            return scheduledArrivalTime;
        }
    }
}