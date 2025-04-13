package com.aims.hospital.service;

import com.aims.hospital.model.Prescription;

public interface PrescriptionServiceInterface {
    void save(Prescription prescription);
    Prescription findByAppointmentId(int appointmentId);
}
