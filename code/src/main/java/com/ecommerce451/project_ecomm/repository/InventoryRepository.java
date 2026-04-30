package com.ecommerce451.project_ecomm.repository;

import com.ecommerce451.project_ecomm.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
}
