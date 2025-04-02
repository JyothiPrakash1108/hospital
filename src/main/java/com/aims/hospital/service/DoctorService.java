package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.repository.AppointmentRepo;
import com.aims.hospital.repository.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DoctorService implements DoctorServiceInterface{
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private AppointmentRepo appointmentRepo;
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
}
