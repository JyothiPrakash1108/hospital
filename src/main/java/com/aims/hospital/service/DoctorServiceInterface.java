package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Doctor;

import java.time.LocalDate;
import java.util.List;

public interface DoctorServiceInterface {
    Doctor findByEmail(String email);
    void save(Doctor doctor);
    void toggleAvailability(Doctor doctor);
    void updateAppointmentStatus(int appointmentId, String status);
    List<String> getAllDepartments();
    List<Doctor> findAvailableDoctorsByDepartmentAndDate(String department, LocalDate localDate);
    Doctor findById(int doctorId);
}
