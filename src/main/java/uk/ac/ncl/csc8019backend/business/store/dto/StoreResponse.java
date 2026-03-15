package uk.ac.ncl.csc8019backend.business.store.dto;

import lombok.Data;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;

/**
 * Store response dto
 * Used for api output
 */
@Data
public class StoreResponse {

    /**
     * Store id
     */
    private Long id;

    /**
     * Store name
     */
    private String name;

    /**
     * Store code
     */
    private String code;

    /**
     * Store location name
     */
    private String locationName;

    /**
     * Store address
     */
    private String address;

    /**
     * Store latitude
     */
    private Double latitude;

    /**
     * Store longitude
     */
    private Double longitude;

    /**
     * Store phone
     */
    private String phone;

    /**
     * Store email
     */
    private String email;

    /**
     * Store status
     */
    private String status;

    /**
     * Legacy opening time
     */
    private String openingTime;

    /**
     * Legacy closing time
     */
    private String closingTime;

    /**
     * Map entity to dto
     */
    public static StoreResponse from(Store store) {
        if (store == null) {
            return null;
        }

        StoreResponse dto = new StoreResponse();
        dto.setId(store.getId());
        dto.setName(store.getName());
        dto.setCode(store.getCode());
        dto.setLocationName(store.getLocationName());
        dto.setAddress(store.getAddress());
        dto.setLatitude(store.getLatitude());
        dto.setLongitude(store.getLongitude());
        dto.setPhone(store.getPhone());
        dto.setEmail(store.getEmail());
        dto.setStatus(store.getStatus());
        dto.setOpeningTime(store.getOpeningTime());
        dto.setClosingTime(store.getClosingTime());
        return dto;
    }
}