package com.ecommerce451.project_ecomm.service;

import com.ecommerce451.project_ecomm.model.Customers;
import com.ecommerce451.project_ecomm.model.Orders;
import com.ecommerce451.project_ecomm.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrdersRepository ordersRepository;

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional
    public void saveOrder(Orders order) {
        ordersRepository.save(order);
    }

    public void toggleOrderStatus(Integer id) {
        Orders order = ordersRepository.findById(id).orElse(null);
        if (order != null) {
            // If it was false, it becomes true (and vice versa)
            order.setStatusFlag(!order.isStatusFlag()); 
            ordersRepository.save(order);
        }
    }

    // Inside OrderService.java
    public List<Orders> getOrdersByCustomer(Customers customer) {
        return ordersRepository.findByCustomer(customer);
    }


}
