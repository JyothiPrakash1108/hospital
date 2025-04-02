package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Doctor;

public interface DoctorServiceInterface {
    Doctor findByEmail(String email);
    void save(Doctor doctor);
    void toggleAvailability(Doctor doctor);
    void updateAppointmentStatus(int appointmentId, String status);
}
