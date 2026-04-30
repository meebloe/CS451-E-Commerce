package com.ecommerce451.project_ecomm.model;

import jakarta.persistence.*;
import lombok.Data;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Customers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // INT PRIMARY KEY auto-increment as customers are registered
    private Integer customerId;

    @Column(unique = true, nullable = false, length = 255)
    private String customerEmail;

    @Column(unique = true, nullable = false, length = 20)
    private String customerNumber;

    @Column(nullable = false, length = 255)
    private String customerPassword;
}
