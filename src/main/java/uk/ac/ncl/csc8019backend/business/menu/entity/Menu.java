package uk.ac.ncl.csc8019backend.business.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Entity
@Table(name = "menu") // Mapping to menu table of database
@Data // Auto create getter & setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock can not be negative")
    private Integer stock;

    @NotNull(message = "State cannot be null")
    private Boolean isAvailable;

    @PrePersist
    @PreUpdate
    private void stockLogic() { // State is unavailable when the stock is 0.
        if (getStock() <= 0) {
            setIsAvailable(false);
        }
    }

}