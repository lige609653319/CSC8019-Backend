package uk.ac.ncl.csc8019backend.business.store.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Store entity
 * Stores basic info business hours location and contact details
 */
@Entity
@Table(name = "store")
@Data
public class Store {

    /**
     * Store id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Store name
     */
    @Column(nullable = false)
    private String name;

    /**
     * Unique store code
     */
    @Column(unique = true, nullable = false)
    private String code;

    /**
     * Location name
     */
    private String locationName;

    /**
     * Full address
     */
    private String address;

    /**
     * Latitude
     */
    private Double latitude;

    /**
     * Longitude
     */
    private Double longitude;

    /**
     * Phone number
     */
    private String phone;

    /**
     * Email address
     */
    private String email;

    /**
     * Store status
     */
    private String status;

    /**
     * Opening time
     */
    private String openingTime;

    /**
     * Closing time
     */
    private String closingTime;
}