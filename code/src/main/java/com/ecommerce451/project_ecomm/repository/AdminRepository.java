package com.ecommerce451.project_ecomm.repository;

import com.ecommerce451.project_ecomm.model.Admins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admins, Integer> {
    Admins findByAdminEmail(String adminEmail);

}
