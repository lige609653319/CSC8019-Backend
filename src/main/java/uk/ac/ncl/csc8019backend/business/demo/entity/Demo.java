package uk.ac.ncl.csc8019backend.business.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "demo")
@Data
public class Demo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;
}
