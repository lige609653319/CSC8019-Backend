package uk.ac.ncl.csc8019backend.business.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Nearby store response dto
 * Contains store info and distance
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreNearbyResponse {

    private StoreResponse store;
    private Double distance;
}