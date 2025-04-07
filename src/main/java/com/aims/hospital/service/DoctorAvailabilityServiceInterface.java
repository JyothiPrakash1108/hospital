package com.aims.hospital.service;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;

import java.time.LocalDate;

public interface DoctorAvailabilityServiceInterface {
    DoctorAvailability getAvailableForDoctorOnDate(Doctor doctor, LocalDate localDate);
}
