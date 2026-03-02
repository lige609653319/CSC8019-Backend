package uk.ac.ncl.csc8019backend.business.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    Optional<Store> findByCode(String code);
    List<Store> findByStatus(String status);
}