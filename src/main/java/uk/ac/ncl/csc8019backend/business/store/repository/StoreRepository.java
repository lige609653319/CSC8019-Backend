package uk.ac.ncl.csc8019backend.business.store.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;

import java.util.List;
import java.util.Optional;

/**
 * Store repository
 * Handles store data access
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * Find a store by code
     */
    Optional<Store> findByCode(String code);

    /**
     * Find stores by status
     */
    List<Store> findByStatus(String status);

    /**
     * Find stores by name
     */
    List<Store> findByNameContainingIgnoreCase(String name);
}