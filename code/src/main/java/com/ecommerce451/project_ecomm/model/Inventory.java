package com.ecommerce451.project_ecomm.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inventory")
@Data
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Lob
    @Column(nullable = true, columnDefinition = "LONGBLOB")
    private byte[] image;


    private boolean discountFlag = false;

    @Column(nullable = false)
    private LocalDate dateAdded;

    @Column(nullable = false)
    private Integer stock;
}
