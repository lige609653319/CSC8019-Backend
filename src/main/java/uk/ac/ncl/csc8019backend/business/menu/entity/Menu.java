package uk.ac.ncl.csc8019backend.business.menu.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "menu") //和数据库中的menu表做映射 mapping to menu table of database
@Data //auto create getter & setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Id generate strategy
    private Long id;

    private String name;

    //什么时候用枚举 when use enum
    private String category;

    private Double priceRegular;
    private Double priceLarge;

}