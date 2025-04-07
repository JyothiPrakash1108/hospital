package com.aims.hospital.controller;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/doctors")
    @ResponseBody
    public List<Doctor> getAvailableDoctors(@RequestParam String department,@RequestParam String date){
        LocalDate localDate = LocalDate.parse(date);
        return doctorService.findAvailableDoctorsByDepartmentAndDate(department,localDate);
    }

}
