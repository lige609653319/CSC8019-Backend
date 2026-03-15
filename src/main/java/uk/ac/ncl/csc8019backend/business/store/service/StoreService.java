package uk.ac.ncl.csc8019backend.business.store.service;

import uk.ac.ncl.csc8019backend.business.store.dto.StoreHoursRequest;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreHoursResponse;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreNearbyResponse;
import uk.ac.ncl.csc8019backend.business.store.dto.StoreResponse;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;

import java.util.List;

/**
 * Store service contract
 * Returns DTOs for API responses
 */
public interface StoreService {

    /**
     * Get all stores
     */
    List<StoreResponse> getAllStores();

    /**
     * Get stores by status
     */
    List<StoreResponse> getStoresByStatus(String status);

    /**
     * Search stores by name
     */
    List<StoreResponse> getStoresByName(String name);

    /**
     * Get stores that are open now
     */
    List<StoreResponse> getOpenStores();

    /**
     * Get nearby stores with distance
     */
    List<StoreNearbyResponse> getNearbyStores(Double latitude, Double longitude, Double radius);

    /**
     * Get store by id
     */
    StoreResponse getStoreById(Long id);

    /**
     * Create store
     */
    StoreResponse createStore(Store store);

    /**
     * Update store by id
     */
    StoreResponse updateStore(Long id, Store store);

    /**
     * Disable store by id
     */
    StoreResponse disableStore(Long id);

    /**
     * Activate store by id
     */
    StoreResponse activateStore(Long id);

    /**
     * Check if store is open now
     */
    boolean isStoreOpen(Long id);

    /**
     * Get weekly hours for a store
     */
    List<StoreHoursResponse> getStoreHours(Long storeId);

    /**
     * Insert or update one day hours
     */
    StoreHoursResponse upsertStoreHours(Long storeId, StoreHoursRequest request);
}