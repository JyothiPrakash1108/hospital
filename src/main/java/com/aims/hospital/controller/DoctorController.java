package com.aims.hospital.controller;

import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import com.aims.hospital.model.Prescription;
import com.aims.hospital.service.AppointmentService;
import com.aims.hospital.service.DoctorAvailabilityService;
import com.aims.hospital.service.DoctorService;
import com.aims.hospital.service.PrescriptionService;
import com.aims.hospital.wrapper.DoctorAvailabilityWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.channels.MulticastChannel;
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
    @Autowired
    private PrescriptionService prescriptionService;
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
    @GetMapping("/appointments")
    public String getAppointments(Model model,Principal principal){
        String email = principal.getName();;
        Doctor doctor = doctorService.findByEmail(email);
        List<Appointment> appointments = appointmentService.findAppointmentByDoctor(doctor);
        model.addAttribute("appointments",appointments);
        return "/doctor/doctor_appointments";
    }
    @GetMapping("/appointments/filter")
    public String filterAppointments(
            @RequestParam(required = false ) String patientName,
            @RequestParam(required = false) @DateTimeFormat LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat LocalDate toDate,Principal principal,Model model){

        String email = principal.getName();
        Doctor doctor = doctorService.findByEmail(email);
        List<Appointment> appointments = appointmentService.filterAppointments(doctor,patientName,fromDate,toDate);
        //debug
        for (Appointment appointment : appointments) {
            System.out.println((appointment.getPatient().getUsername()));
        }
        model.addAttribute("appointments",appointments);
        return "fragments/appointments :: appointmentTable";
    }
    @PostMapping("/prescription/add")
    public String uploadPrescription(@RequestParam("appointmentId") int appointmentId,
                                     @RequestParam("description") String description,
                                     @RequestParam("file")MultipartFile multipartFile){
        Appointment appointment = appointmentService.findById(appointmentId);
        Prescription prescription = new Prescription();
        prescription.setDescription(description);
        prescription.setAppointment(appointment);
        prescription.setFileType(multipartFile.getContentType());
        try {
            prescription.setFileData(multipartFile.getBytes());
            prescriptionService.save(prescription);
            System.out.println("setting file data ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "redirect:/doctor/appointments";
    }
    @GetMapping("/prescription/view/{appointmentId}")
    public ResponseEntity<byte[]> viewPresscription(@PathVariable int appointmentId){
        Prescription prescription = prescriptionService.findByAppointmentId(appointmentId);
        byte[] fileData = prescription.getFileData();
        String fileType = prescription.getFileType();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(fileType));
        httpHeaders.setContentDisposition(ContentDisposition.inline()
                .filename("prescription_"+appointmentId+getExtensionFromMimeType(fileType)).build());
        return new ResponseEntity<>(fileData, httpHeaders, HttpStatus.OK);
    }
    private String getExtensionFromMimeType(String mimeType) {
        switch (mimeType) {
            case "application/pdf":
                return ".pdf";
            case "image/jpeg":
                return ".jpg";
            case "image/png":
                return ".png";
            default:
                return "";
        }
    }

}
