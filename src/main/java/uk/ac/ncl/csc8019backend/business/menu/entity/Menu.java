package uk.ac.ncl.csc8019backend.business.menu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import uk.ac.ncl.csc8019backend.business.store.entity.Store;

import java.util.List;

@Entity
@Table(name = "menu") // Mapping to menu table of database
@Data // Auto create getter & setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name can not be blank")
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MenuSku> skus;

    /**
     * Store that owns this menu item.
     * Each store can have multiple menu items and menus may vary between stores.
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

}