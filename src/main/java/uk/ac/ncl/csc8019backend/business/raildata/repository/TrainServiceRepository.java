package uk.ac.ncl.csc8019backend.business.raildata.repository;

import uk.ac.ncl.csc8019backend.business.raildata.entity.TrainService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainServiceRepository extends JpaRepository<TrainService, Long> {

    Optional<TrainService> findByTrainId(String trainId);

    List<TrainService> findByIsActiveTrue();

    List<TrainService> findByStatus(String status);

    List<TrainService> findByScheduledArrivalTimeBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT t FROM TrainService t WHERE t.delayMinutes > 0 AND t.isActive = true")
    List<TrainService> findDelayedTrains();

    @Query("SELECT t FROM TrainService t WHERE t.scheduledArrivalTime BETWEEN :now AND :futureTime AND t.isActive = true ORDER BY t.scheduledArrivalTime ASC")
    List<TrainService> findUpcomingTrains(@Param("now") LocalDateTime now, @Param("futureTime") LocalDateTime futureTime);

    List<TrainService> findByOriginStationContainingAndIsActiveTrue(String stationName);

    List<TrainService> findByDestinationStationContainingAndIsActiveTrue(String stationName);

    @Query("SELECT DISTINCT t.originStation FROM TrainService t WHERE t.isActive = true UNION SELECT DISTINCT t.destinationStation FROM TrainService t WHERE t.isActive = true")
    List<String> findAllStationNames();

    @Query("SELECT t FROM TrainService t WHERE t.originStation = :origin AND t.destinationStation = :destination AND t.isActive = true")
    List<TrainService> findByRoute(@Param("origin") String origin, @Param("destination") String destination);
}