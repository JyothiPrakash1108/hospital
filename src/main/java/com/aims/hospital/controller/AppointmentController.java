package com.aims.hospital.controller;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;
import com.aims.hospital.service.AppointmentService;
import com.aims.hospital.service.DoctorService;
import com.aims.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PatientService patientService;

    @GetMapping("/doctors")
    @ResponseBody
    public List<Doctor> getAvailableDoctors(@RequestParam String department,@RequestParam String date){
        LocalDate localDate = LocalDate.parse(date);
        return doctorService.findAvailableDoctorsByDepartmentAndDate(department,localDate);
    }
    @GetMapping("/available-slots")
    @ResponseBody
    public List<String> getAvailableTimeSlots(@RequestParam int doctorId,@RequestParam String date){
        Doctor doctor = doctorService.findById(doctorId);
        LocalDate localDate = LocalDate.parse(date);
        return appointmentService.getAvailableSlots(doctor,localDate)
                .stream()
                .map(LocalTime::toString)
                .toList();
    }
    @PostMapping("/book")
    @ResponseBody
    public ResponseEntity<String> bookAppointment(@RequestParam int doctorId, @RequestParam String date, @RequestParam("timeSlot") String time, Principal principal){
        Patient patient = patientService.findPatientByEmail(principal.getName());
        Doctor doctor = doctorService.findById(doctorId);
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);
        appointmentService.bookApointment(patient,doctor,localDate,localTime);
        return ResponseEntity.ok("Appontment booked sucessfully");
    }


}
