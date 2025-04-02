package com.aims.hospital.controller;

import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.service.AppointmentService;
import com.aims.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @GetMapping("/dashboard")
    public String getDoctorDashboard(Model model, Principal principal){
        Doctor doctor = doctorService.findByEmail(principal.getName());
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctorToday(doctor.getId());
        model.addAttribute("doctor",doctor);
        model.addAttribute("totalPatientsToday",appointments.size());
        model.addAttribute("appointments",appointments);
        return "/doctor/doctor-dashboard.html";
    }
    @PostMapping("/toggle-availability")
    public String toogleAvailability(Principal principal){
        Doctor doctor = doctorService.findByEmail(principal.getName());
        doctorService.toggleAvailability(doctor);
        return "redirect:/doctor/dashboard";
    }
    @PostMapping("/appointment/{id}/status")
    public String updateAppointmentStatus(@PathVariable("id") int appointmentId, @RequestParam("status") String status){
        doctorService.updateAppointmentStatus(appointmentId,status);
        return "redirect:/doctor/dashboard";
    }
}
