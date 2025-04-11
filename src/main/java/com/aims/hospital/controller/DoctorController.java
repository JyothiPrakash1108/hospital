package com.aims.hospital.controller;

import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import com.aims.hospital.service.AppointmentService;
import com.aims.hospital.service.DoctorAvailabilityService;
import com.aims.hospital.service.DoctorService;
import com.aims.hospital.wrapper.DoctorAvailabilityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/doctor")
@Transactional
public class DoctorController {
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setAutoGrowNestedPaths(true);
    }

    @GetMapping("/dashboard")
    @Transactional(readOnly = true)
    public String getDoctorDashboard(Model model, Principal principal){
       /* Doctor doctor = doctorService.findByEmail(principal.getName());
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctorToday(doctor.getId());
        model.addAttribute("doctor",doctor);
        model.addAttribute("totalPatientsToday",appointments.size());
        model.addAttribute("appointments",appointments);
        DoctorAvailabilityWrapper wrapper = new DoctorAvailabilityWrapper();
        wrapper.setAvailabilities(doctorAvailabilityService.getAvailabilityForNext4Days(doctor));
        model.addAttribute("wrapper",wrapper);
        return "/doctor/doctor-dashboard.html";*/
        String email = principal.getName();
        Doctor doctor = doctorService.findByEmail(email);

        List<DoctorAvailability> availabilities = doctorAvailabilityService.getAvailabilityForNext4Days(doctor);
        System.out.println("Fetched from DB:");
        availabilities.forEach(a -> System.out.println(a.getDate() + " | " + a.getStartTime() + " - " + a.getEndTime() + " | " + a.isAvailable()));

        List<LocalDate> next4Dates = IntStream.range(0, 4)
                .mapToObj(i -> LocalDate.now().plusDays(i))
                .collect(Collectors.toList());

        Map<LocalDate, DoctorAvailability> availabilityMap = availabilities.stream()
                .collect(Collectors.toMap(DoctorAvailability::getDate, a -> a));

        List<DoctorAvailability> finalAvailabilityList = new ArrayList<>();

        for (LocalDate date : next4Dates) {
            if (availabilityMap.containsKey(date)) {
                finalAvailabilityList.add(availabilityMap.get(date));
            } else {
                finalAvailabilityList.add(new DoctorAvailability(date, LocalTime.of(9, 0), LocalTime.of(17, 0), false, doctor));
            }
        }


        DoctorAvailabilityWrapper wrapper = new DoctorAvailabilityWrapper();
        wrapper.setAvailabilities(finalAvailabilityList);
        System.out.println("Final availability list (passed to UI):");
        for (DoctorAvailability av : finalAvailabilityList) {
            System.out.println(av.getDate() + " | " + av.getStartTime() + " - " + av.getEndTime() + " | " + av.isAvailable());
        }
        model.addAttribute("wrapper", wrapper);

        // Also pass other dashboard data as needed
        List<Appointment> appointments = appointmentService.getAppointmentsForDoctorToday(doctor.getId());
        model.addAttribute("doctor",doctor);
        model.addAttribute("totalPatientsToday",appointments.size());
        model.addAttribute("appointments",appointments);

        System.out.println("------------------ " );
        finalAvailabilityList.forEach(av -> System.out.println(av.getDate() + " | " + av.getStartTime() + " - " + av.getEndTime() + " | " + av.isAvailable()));

        return "/doctor/doctor-dashboard";
    }

    @PostMapping("/appointment/{id}/status")
    public String updateAppointmentStatus(@PathVariable("id") int appointmentId, @RequestParam("status") String status){
        doctorService.updateAppointmentStatus(appointmentId,status);
        return "redirect:/doctor/dashboard";
    }
    @PostMapping("/setAvailability")
    @Transactional
    public String setAvailability(@ModelAttribute("wrapper") DoctorAvailabilityWrapper wrapper, Principal principal) {
        System.out.println("Starting setAvailability with data:");
        wrapper.getAvailabilities().forEach(a -> System.out.println(a.getDate() + " | " + a.getStartTime() + " - " + a.getEndTime() + " | " + a.isAvailable()));

        Doctor doctor = doctorService.findByEmail(principal.getName());
        doctorAvailabilityService.saveAll(wrapper.getAvailabilities(), doctor);


        System.out.println("After saveAll, fetching fresh data:");
        List<DoctorAvailability> freshData = doctorAvailabilityService.getAvailabilityForNext4Days(doctor);
        freshData.forEach(a -> System.out.println(a.getDate() + " | " + a.getStartTime() + " - " + a.getEndTime() + " | " + a.isAvailable()));
     return "redirect:/doctor/dashboard";
    }

}
