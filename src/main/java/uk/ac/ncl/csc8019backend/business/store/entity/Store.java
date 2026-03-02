package uk.ac.ncl.csc8019backend.business.store.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "store")
@Data
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    private String locationName;

    private String status;

    private String openingTime;

    private String closingTime;
}