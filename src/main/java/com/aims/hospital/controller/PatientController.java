package com.aims.hospital.controller;

import com.aims.hospital.model.OTP;
import com.aims.hospital.model.Patient;
import com.aims.hospital.service.EmailService;
import com.aims.hospital.service.OtpService;
import com.aims.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;
    private OtpService otpService;
    private EmailService emailService;
    @Autowired
    public PatientController(PatientService patientService,OtpService otpService,EmailService emailService){
        this.patientService = patientService;
        this.otpService = otpService;
        this.emailService = emailService;
    }

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
        Patient patient1 = patientService.findPatientByEmail(patient.getEmail());
        if (patient1 != null){
            model.addAttribute("error","Email already exists! please login");
            return "patient/login";
        }
        String otp = otpService.generateOtp();
        emailService.sendEmail(patient.getEmail(),otp);
        otpService.saveOtp(otp,patient.getEmail());
        patientService.addPatient(patient);
        model.addAttribute("email",patient.getEmail());
        return "patient/verify";
    }

    @PostMapping("/verification")
    public String otpVerification(@RequestParam String email,@RequestParam String otp,Model model){
        Patient patient = patientService.findPatientByEmail(email);
        OTP otp1 = otpService.fetchByEmail(email);
        System.out.println(patient.getName());
        System.out.println(otp1.getOtp());
        if (otpService.validateOtp(otp,email)){
            patient.setVerified(true);
            otpService.deleteRecord(otp1);
            model.addAttribute("message","verification sucessful");
            return "patient/login";
        }
        model.addAttribute("error","OTP expired or invalid");
        model.addAttribute("email",email);
        return "patient/verify";
    }
}
