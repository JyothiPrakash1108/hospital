package com.aims.hospital.service;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.repository.AppointmentRepo;
import com.aims.hospital.repository.DoctorRepo;
import com.aims.hospital.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AdminService implements AdminServiceInterface{
    @Autowired
    private PatientRepo patientRepo;
    @Autowired
    private DoctorRepo doctorRepo;
    @Autowired
    private AppointmentRepo appointmentRepo;

    @Override
    public int getTotalPatientsToday() {
        return appointmentRepo.countAppointmentsByDate(LocalDateTime.now());
    }

    @Override
    public int getAvailableDoctorsCount() {
        List<Doctor> doctors = doctorRepo.findByIsAvailableTrue();
        int count = 0;
        for(Doctor doctor:doctors){
            count++;
        }
        return count;
    }

    @Override
    public List<Doctor> getAvailableDoctors() {
        return doctorRepo.findByIsAvailableTrue();
    }
}
