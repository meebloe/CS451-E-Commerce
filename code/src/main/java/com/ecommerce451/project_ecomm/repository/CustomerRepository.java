package com.ecommerce451.project_ecomm.repository;

import com.ecommerce451.project_ecomm.model.Customers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {
}
