package com.ecommerce451.project_ecomm.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "admins")
@Data // If you use Lombok, otherwise manually add Getters/Setters
public class Admins {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adminId;

    @Column(unique = true, nullable = false)
    private String adminEmail;

    @Column(unique = true, nullable = false, length = 20)
    private String adminNumber;

    @Column(nullable = false)
    private String adminPassword;
}
