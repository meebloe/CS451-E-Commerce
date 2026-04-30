package com.ecommerce451.project_ecomm.service;

import com.ecommerce451.project_ecomm.model.Admins;
import com.ecommerce451.project_ecomm.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public void saveAdmin(Admins admin) {
        adminRepository.save(admin);
    }

    public List<Admins> getAllAdmins() {
        return adminRepository.findAll();
    }

    public void deleteAdmin(Integer id) {
        adminRepository.deleteById(id);
    }

    public Admins getAdminById(Integer id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Admins getAdminByEmail(String email) {
        return adminRepository.findByAdminEmail(email);
    }


}
