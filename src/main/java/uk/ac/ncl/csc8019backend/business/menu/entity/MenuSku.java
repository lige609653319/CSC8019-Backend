package uk.ac.ncl.csc8019backend.business.menu.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "menu_sku")
@Getter
@Setter
public class MenuSku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Size size;

    @NotNull
    @Column(nullable = false)
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Stock cannot be null")
    @Column(nullable = false)
    @Min(value = 0, message = "Stock can not be negative")
    private Integer stock;

    @NotNull(message = "State cannot be null")
    @Column(nullable = false)
    private Boolean isAvailable;

    @PrePersist
    @PreUpdate
    private void stockLogic() { // State is unavailable when the stock is 0.
        if (stock != null && stock <= 0) {
            setIsAvailable(false);
        }
    }

}
