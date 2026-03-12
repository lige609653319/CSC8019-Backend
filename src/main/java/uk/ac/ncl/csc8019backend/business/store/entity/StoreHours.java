package uk.ac.ncl.csc8019backend.business.store.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

/**
 * Store hours entity
 * Stores weekly hours per store
 */
@Entity
@Table(
        name = "store_hours",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_store_day", columnNames = {"store_id", "day_of_week"})
        }
)
@Data
public class StoreHours {

    /**
     * Store id
     */
    @Column(name = "store_id", nullable = false, insertable = false, updatable = false)
    private Long storeId;

    /**
     * Store hours id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Store reference
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /**
     * Day of week
     * 1 Monday 7 Sunday
     */
    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek;

    /**
     * Open flag
     */
    @Column(name = "is_open", nullable = false)
    private Boolean isOpen;

    /**
     * Open time
     */
    @Column(name = "open_time")
    private LocalTime openTime;

    /**
     * Close time
     */
    @Column(name = "close_time")
    private LocalTime closeTime;
}