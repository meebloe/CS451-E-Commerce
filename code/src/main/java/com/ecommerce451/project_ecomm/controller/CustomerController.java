package com.ecommerce451.project_ecomm.controller;

import com.ecommerce451.project_ecomm.model.Customers;
import com.ecommerce451.project_ecomm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {
    @Autowired
    private CustomerService service;

    @GetMapping("/")
    public String viewHome(Model model) {
        model.addAttribute("customers", service.getAll());
        model.addAttribute("newCustomer", new Customers());
        return "index";
    }

    @PostMapping("/add")
    public String addCustomer(@ModelAttribute Customers customer, RedirectAttributes ra) {
        String result = service.save(customer);
        ra.addFlashAttribute("message", result);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
        service.delete(id);
        ra.addFlashAttribute("message", "Customer deleted successfully!");
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String editCustomer(@PathVariable Integer id, Model model) {
        model.addAttribute("customers", service.getAll());
        model.addAttribute("newCustomer", service.getById(id)); // Pre-fills the form
        return "index";
    }
}
