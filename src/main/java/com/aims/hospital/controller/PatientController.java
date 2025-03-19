package com.aims.hospital.controller;

import com.aims.hospital.model.Patient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientController {
    @GetMapping("/login")
    public String getLoginForm(){
        return "patient/login";
    }
    @GetMapping("/register")
    public String getRegistrationForm(Model model){
        model.addAttribute("patient",new Patient());
        return "patient/register";
    }
    @PostMapping("/register")
    public String generateOtp(@ModelAttribute Patient patient,Model model){
        System.out.println(patient.getName());
        System.out.println(patient.getGender());
        return "patient/verify";
    }
}
