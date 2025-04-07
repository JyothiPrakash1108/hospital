package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import com.aims.hospital.repository.AppointmentRepo;
import com.aims.hospital.repository.DoctorAvailabilityRepo;
import com.aims.hospital.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DoctorService implements DoctorServiceInterface{
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
   private DoctorAvailabilityRepo doctorAvailabilityRepo;
    @Override
    public Doctor findByEmail(String email) {
        return doctorRepo.findByEmail(email);
    }

    @Override
    public void save(Doctor doctor) {
        doctorRepo.save(doctor);
    }

    @Override
    public void toggleAvailability(Doctor doctor) {
        doctor.setAvailable(!doctor.isAvailable());
        doctorRepo.save(doctor);
    }

    @Override
    public void updateAppointmentStatus(int appointmentId, String status) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElse(null);
        if(appointment!=null){
            appointment.setStatus(Status.valueOf(status));
            appointmentRepo.save(appointment);
        }

    }

    @Override
    public List<String> getAllDepartments() {
        return doctorRepo.findAll()
                .stream()
                .map(Doctor::getDepartment)
                .distinct()
                .toList();
    }

    @Override
    public List<Doctor> findAvailableDoctorsByDepartmentAndDate(String department, LocalDate localDate) {
       List<Doctor> doctors = doctorRepo.findByDepartmentAndIsAvailableTrue(department);
       return doctors.stream()
               .filter(
                       doctor -> {
                           DoctorAvailability doctorAvailability = doctorAvailabilityRepo.findByDoctorAndDate(doctor,localDate)
                                   .orElse(null);
                           return (doctorAvailability == null || doctorAvailability.isAvailable());
                       }
               )
               .toList();
    }
}
