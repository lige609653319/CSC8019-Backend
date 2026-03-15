package uk.ac.ncl.csc8019backend.business.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.csc8019backend.business.store.entity.StoreHours;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreHoursRepository extends JpaRepository<StoreHours, Long> {

    Optional<StoreHours> findByStoreIdAndDayOfWeek(Long storeId, Integer dayOfWeek);

    List<StoreHours> findByDayOfWeek(Integer dayOfWeek);

    List<StoreHours> findByStoreId(Long storeId);
}