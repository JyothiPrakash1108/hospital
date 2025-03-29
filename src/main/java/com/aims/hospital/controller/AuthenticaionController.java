package com.aims.hospital.controller;

import com.aims.hospital.model.Patient;
import com.aims.hospital.service.EmailService;
import com.aims.hospital.service.OtpService;
import com.aims.hospital.service.PatientService;
import com.aims.hospital.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthenticaionController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;
    @Autowired
    private PatientService patientService;

    @GetMapping("/login")
    public String getLoginForm(@RequestParam(value = "error",required = false)String error, Model model){
        if(error != null){
            model.addAttribute("error","invalid credentials");
        }
        return "auth/login";
    }
    @GetMapping("/register")
    public String getPatientRegistrationForm(Model model){
        model.addAttribute("patient",new Patient());
        return "patient/register";
    }
    @PostMapping("/register")
    public String registerPatientAndGenerateOtp(@Valid@ModelAttribute("patient") Patient patient, BindingResult result,Model model){
        Patient existingPatient = patientService.findPatientByEmail(patient.getEmail());
        if(existingPatient != null){
            if(existingPatient.isVerified()){
                model.addAttribute("error","Email is already registered. Try another one.");
                return "patient/register";
            }
            else{
                model.addAttribute("message","verify to continue");
                String otp = otpService.generateOtp();
                otpService.saveOtp(otp,patient.getEmail());
                emailService.sendEmail(patient.getEmail(),otp);
                model.addAttribute("email",patient.getEmail());
                return "patient/verify";
            }
        }
        userService.registerPatient(patient);
        String otp = otpService.generateOtp();
        otpService.saveOtp(otp,patient.getEmail());
        emailService.sendEmail(patient.getEmail(),otp);
        model.addAttribute("email",patient.getEmail());
        return "patient/verify";
    }
    @PostMapping("/verify")
    public String otpVerification(@RequestParam("email")String email,@RequestParam("otp")String otp,Model model){
        boolean status = otpService.validateOtp(otp,email);
        Patient patient = patientService.findPatientByEmail(email);
        if(status){
            patientService.verifyPatient(patient);
            model.addAttribute("message","verification sucessful , log in to continue");
            return "auth/login";
        }
        else {
            model.addAttribute("error","invalid or otp expired");
            model.addAttribute("email",email);
            return "patient/verify";
        }
    }
}
