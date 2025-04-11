package com.aims.hospital.service;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DoctorAvailabilityServiceInterface {
    DoctorAvailability getAvailableForDoctorOnDate(Doctor doctor, LocalDate localDate);
    List<DoctorAvailability> getUpcomingAvailability(Doctor doctor);
    void saveAvailabilitySlots(Doctor doctor, List<DoctorAvailability> slots);
    Optional<DoctorAvailability> findByDoctorAndDate(Doctor doctor, LocalDate date);
    List<DoctorAvailability> getAvailabilityForNext4Days(Doctor doctor);
    void saveAll(List<DoctorAvailability> availabilities, Doctor doctor);


}
