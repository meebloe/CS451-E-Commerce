package com.ecommerce451.project_ecomm.service;

import com.ecommerce451.project_ecomm.model.Inventory;
import com.ecommerce451.project_ecomm.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    public List<Inventory> getAllItems() {
        return inventoryRepository.findAll();
    }

    public void saveItem(Inventory item) {
        inventoryRepository.save(item);
    }

    public void deleteItem(Integer id) {
        inventoryRepository.deleteById(id);
    }

    public Inventory getItemById(Integer id) {
        return inventoryRepository.findById(id).orElse(null);
    }


}
