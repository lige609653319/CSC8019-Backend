package uk.ac.ncl.csc8019backend.business.store.service;

import uk.ac.ncl.csc8019backend.business.store.dto.StoreNearbyResponse;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;

import java.util.List;

/**
 * Store service
 * Defines store business operations
 */
public interface StoreService {

    /**
     * Get all stores
     */
    List<Store> getAllStores();

    /**
     * Get stores by status
     */
    List<Store> getStoresByStatus(String status);

    /**
     * Get stores by name
     */
    List<Store> getStoresByName(String name);

    /**
     * Get open stores
     */
    List<Store> getOpenStores();

    /**
     * Get nearby stores
     */
    List<StoreNearbyResponse> getNearbyStores(Double latitude, Double longitude, Double radius);

    /**
     * Get store by id
     */
    Store getStoreById(Long id);

    /**
     * Create a store
     */
    Store createStore(Store store);

    /**
     * Update a store
     */
    Store updateStore(Long id, Store store);

    /**
     * Disable a store
     */
    Store disableStore(Long id);

    /**
     * Activate a store
     */
    Store activateStore(Long id);

    /**
     * Check store open status
     */
    boolean isStoreOpen(Long id);
}