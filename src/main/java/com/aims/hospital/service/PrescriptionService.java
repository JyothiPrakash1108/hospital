package com.aims.hospital.service;

import com.aims.hospital.model.Prescription;
import com.aims.hospital.repository.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService implements PrescriptionServiceInterface{
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PrescriptionRepo prescriptionRepo;
    @Override
    public void save(Prescription prescription) {
        prescriptionRepo.save(prescription);
    }

    @Override
    public Prescription findByAppointmentId(int appointmentId) {
        return prescriptionRepo.findByAppointment(appointmentService.findById(appointmentId));
    }
}
