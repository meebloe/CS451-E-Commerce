package com.ecommerce451.project_ecomm.repository;

import com.ecommerce451.project_ecomm.model.Customers;
import com.ecommerce451.project_ecomm.model.Orders;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    List<Orders> findByCustomer(Customers customer);
}
