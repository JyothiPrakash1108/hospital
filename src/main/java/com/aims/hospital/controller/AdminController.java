package com.aims.hospital.controller;

import com.aims.hospital.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @GetMapping("/dashboard")
    public String getPatientDashboard(Model model) {
        model.addAttribute("totalPatientsToday",adminService.getTotalPatientsToday());
        model.addAttribute("availableDoctorsCount",adminService.getAvailableDoctorsCount());
        model.addAttribute("availableDoctors",adminService.getAvailableDoctors());
        return "/admin/admin-dashboard.html";
    }
}
