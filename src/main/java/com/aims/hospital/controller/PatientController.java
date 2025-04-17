package com.aims.hospital.controller;


import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;
import com.aims.hospital.service.AppointmentService;
import com.aims.hospital.service.DoctorService;
import com.aims.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/patient")
public class PatientController {
    @Autowired
    private PatientService patientService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/dashboard")
    public String getPatientDashboard(Model model, Principal principal) {
        String email = principal.getName();
        Patient patient = patientService.findPatientByEmail(email);
        Set<Doctor> doctors = new HashSet<>(appointmentService.findDoctorsWithCompletedAppointments(patient));
        model.addAttribute("upcomingAppointments", appointmentService.countUpcomingAppointments(patient.getId()));
        model.addAttribute("pastAppointments", appointmentService.countPastAppointments(patient.getId()));
        model.addAttribute("appointments", appointmentService.getAppointmentsByPatientId(patient.getId()));
        model.addAttribute("consultedDoctors",doctors);
        return "/patient/patient-dashboard.html";
    }
    @GetMapping("/book-appointment")
    public String getBookingForm(Model model){
        model.addAttribute("departments",doctorService.getAllDepartments());
        return "/patient/appointment";
    }










/*
    @GetMapping("/login")
    public String getLoginForm(){
        return "auth/login";
    }
    @GetMapping("/register")
    public String getRegistrationForm(Model model){
        model.addAttribute("patient",new Patient());
        return "patient/register";
    }


    @PostMapping("/register")
    public String generateOtp(@ModelAttribute Patient patient,Model model){
        Patient patient1 = patientService.findPatientByEmail(patient    .getEmail());
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
    }*/
}
