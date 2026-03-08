package uk.ac.ncl.csc8019backend.business.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;

/**
 * Nearby store response
 * Contains store data and distance
 */
@Data
@AllArgsConstructor
public class StoreNearbyResponse {

    /**
     * Store data
     */
    private Store store;

    /**
     * Distance in kilometers
     */
    private double distance;
}