package uk.ac.ncl.csc8019backend.business.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "menu") //和数据库中的menu表做映射 mapping to menu table of database
@Data //auto create getter & setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id generate strategy
    private Long id;

    @NotBlank(message = "Name can not be blank")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @Positive(message = "Price must be positive")
    private Double regularPrice;
    @Positive(message = "Price must be positive")
    private Double largePrice;

    @Min(value = 0, message = "Stock can not be negative")
    private Integer stock;
    private Boolean isAvailable;

}