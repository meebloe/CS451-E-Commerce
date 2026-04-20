package com.ecommerce451.project_ecomm.service;

import com.ecommerce451.project_ecomm.model.Customers;
import com.ecommerce451.project_ecomm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    public List<Customers> getAll() { return repository.findAll(); }

    public String save(Customers customer) {
        // 1. Find any customer that already has this email
        Customers existing = repository.findAll().stream()
            .filter(c -> c.getCustomerEmail().equals(customer.getCustomerEmail()))
            .findFirst()
            .orElse(null);

        // 2. Business Rule: It's only a duplicate if the ID is different
        // (If existing is null, it's a brand new email -> OK)
        // (If existing.id matches customer.id, you're just updating yourself -> OK)
        if (existing != null && !existing.getCustomerId().equals(customer.getCustomerId())) {
            return "Error: Email already exists!";
        }

        repository.save(customer);
        return "Success: Customer saved!";
    }


    public void delete(Integer id) {
        // Edge Case: Handle deleting non-existent record
        if (repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

    public Customers getById(Integer id) {
    return repository.findById(id).orElse(null);
    }

}

