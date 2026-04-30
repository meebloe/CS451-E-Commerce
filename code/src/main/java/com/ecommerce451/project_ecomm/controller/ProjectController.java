package com.ecommerce451.project_ecomm.controller;

import com.ecommerce451.project_ecomm.model.Admins;
import com.ecommerce451.project_ecomm.model.Customers;
import com.ecommerce451.project_ecomm.model.Inventory;
import com.ecommerce451.project_ecomm.model.Orders;
import com.ecommerce451.project_ecomm.service.AdminService;
import com.ecommerce451.project_ecomm.service.CustomerService;
import com.ecommerce451.project_ecomm.service.InventoryService;
import com.ecommerce451.project_ecomm.service.OrderService;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProjectController {

    @Autowired private CustomerService customerService;
    @Autowired private AdminService adminService;
    @Autowired private InventoryService inventoryService;
    @Autowired private OrderService orderService;

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    // --- CUSTOMER PORTAL & LOGIN ---
    @GetMapping("/customers")
    public String showCustomerPortal(Model model) {
        model.addAttribute("customers", customerService.getAll());
        model.addAttribute("newCustomer", new Customers());
        return "customers";
    }

    @PostMapping("/customers/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Customers customer = customerService.getCustomerByEmail(email);
        if (customer != null && customer.getCustomerPassword().equals(password)) {
            session.setAttribute("loggedInCustomer", customer);
            return "redirect:/shop"; 
        } else {
            model.addAttribute("message", "Error: Invalid email or password");
            model.addAttribute("customers", customerService.getAll());
            model.addAttribute("newCustomer", new Customers());
            return "customers"; 
        }
    }

    @PostMapping("/add")
    public String addCustomer(@ModelAttribute("newCustomer") Customers customer) {
        customerService.save(customer);
        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("newCustomer", customerService.getById(id));
        model.addAttribute("customers", customerService.getAll());
        return "customers";
    }

    // --- CUSTOMER SHOPPING ---
    @GetMapping("/shop")
    public String showShop(Model model, HttpSession session) {
        Customers customer = (Customers) session.getAttribute("loggedInCustomer");
        if (customer == null) return "redirect:/customers";
    
        model.addAttribute("items", inventoryService.getAllItems());
    
        // FETCH DIRECTLY FROM DB: Use the ID to get the freshest list
        model.addAttribute("myOrders", orderService.getOrdersByCustomer(customer));
    
        return "shop";
    }


    // --- ADMIN PORTAL & LOGIN ---
    @GetMapping("/admins")
    public String showAdminPortal(Model model) {
        model.addAttribute("admins", adminService.getAllAdmins());
        model.addAttribute("newAdmin", new Admins());
        return "admins";
    }

    @PostMapping("/admins/login")
    public String adminLogin(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        Admins admin = adminService.getAdminByEmail(email);
        if (admin != null && admin.getAdminPassword().equals(password)) {
            session.setAttribute("loggedInAdmin", admin);
            return "redirect:/admin/dashboard"; // Redirect to the new dashboard
        } else {
            model.addAttribute("message", "Error: Invalid credentials");
            return "admins"; 
        }
    }

    @GetMapping("/admin/dashboard")
    public String showDashboard(Model model, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
    
        model.addAttribute("totalProducts", inventoryService.getAllItems().size());
        model.addAttribute("totalOrders", orderService.getAllOrders().size());
        model.addAttribute("totalCustomers", customerService.getAll().size()); // Added this
    
        long pendingCount = orderService.getAllOrders().stream()
                                    .filter(o -> !o.isStatusFlag()).count();
        model.addAttribute("pendingOrders", pendingCount);
    
        return "dashboard"; 
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // --- SECURED INVENTORY (ADMIN ONLY) ---
    @GetMapping("/inventory")
    public String showInventory(Model model, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        model.addAttribute("items", inventoryService.getAllItems());
        model.addAttribute("newItem", new Inventory());
        return "inventory";
    }

    @PostMapping("/inventory/add")
    public String addInventoryItem(@ModelAttribute("newItem") Inventory item, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        inventoryService.saveItem(item);
        return "redirect:/inventory";
    }

    @GetMapping("/inventory/edit/{id}")
    public String editInventory(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        model.addAttribute("newItem", inventoryService.getItemById(id));
        model.addAttribute("items", inventoryService.getAllItems());
        return "inventory";
    }

    @GetMapping("/inventory/delete/{id}")
    public String deleteInventoryItem(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        inventoryService.deleteItem(id);
        return "redirect:/inventory";
    }

    // --- SECURED ADMIN MANAGEMENT (ADMIN ONLY) ---
    @PostMapping("/admins/add")
    public String addAdmin(@ModelAttribute("newAdmin") Admins admin, HttpSession session) {
        // Allow adding if no admin exists (first run) OR if an admin is logged in
        if (adminService.getAllAdmins().isEmpty() || session.getAttribute("loggedInAdmin") != null) {
            adminService.saveAdmin(admin);
            return "redirect:/admins";
        }
        return "redirect:/admins";
    }

    @GetMapping("/admins/edit/{id}")
    public String editAdmin(@PathVariable Integer id, Model model, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        model.addAttribute("newAdmin", adminService.getAdminById(id));
        model.addAttribute("admins", adminService.getAllAdmins());
        return "admins"; 
    }

    @GetMapping("/admins/delete/{id}")
    public String deleteAdmin(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        adminService.deleteAdmin(id);
        return "redirect:/admins";
    }

    // --- SECURED ORDERS (ADMIN ONLY FOR VIEWING/TOGGLING) ---
    @GetMapping("/orders")
    public String showOrders(Model model, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders"; 
    }

    @GetMapping("/orders/toggle/{id}")
    public String toggleOrder(@PathVariable Integer id, HttpSession session) {
        if (session.getAttribute("loggedInAdmin") == null) return "redirect:/admins";
        orderService.toggleOrderStatus(id);
        return "redirect:/orders";
    }

    @GetMapping("/my-orders")
    public String showMyOrders(Model model, HttpSession session) {
        Customers customer = (Customers) session.getAttribute("loggedInCustomer");
    
        if (customer == null) {
            return "redirect:/customers";
        }

        // Pass ONLY this customer's orders
        model.addAttribute("myOrders", orderService.getOrdersByCustomer(customer));
        return "my-orders"; 
    }

    @GetMapping("/orders/place/{itemId}")
    public String placeOrder(@PathVariable Integer itemId, HttpSession session) {
        Customers customer = (Customers) session.getAttribute("loggedInCustomer");
        if (customer == null) return "redirect:/customers";

        Inventory item = inventoryService.getItemById(itemId);
    
        // EDGE CASE: Check if stock is available
        if (item.getStock() > 0) {
            // 1. Decrement stock
            item.setStock(item.getStock() - 1);
            inventoryService.saveItem(item); // Update inventory in DB

            // 2. Create the order
            Orders newOrder = new Orders();
            newOrder.setCustomer(customer);
            newOrder.setTotal(item.getPrice());
            newOrder.setDate(LocalDate.now());
            newOrder.setStatusFlag(false);

            orderService.saveOrder(newOrder);
            return "redirect:/my-orders";
        } else {
            // Handle out-of-stock scenario
            return "redirect:/shop?error=OutOfStock";
        }
    }

}
