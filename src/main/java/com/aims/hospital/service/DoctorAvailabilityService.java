package com.aims.hospital.service;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import com.aims.hospital.repository.DoctorAvailabilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DoctorAvailabilityService  implements DoctorAvailabilityServiceInterface{
    @Autowired
    private DoctorAvailabilityRepo doctorAvailabilityRepo;

    @Override
    public DoctorAvailability getAvailableForDoctorOnDate(Doctor doctor, LocalDate localDate) {
        return doctorAvailabilityRepo.findByDoctorAndDate(doctor, localDate)
                .orElseGet(() -> {
                    DoctorAvailability availability = new DoctorAvailability();
                    availability.setDoctor(doctor);
                    availability.setLocalDate(localDate);
                    return availability;
                });
    }
}
