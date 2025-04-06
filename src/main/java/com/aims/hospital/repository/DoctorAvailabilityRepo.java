package com.aims.hospital.repository;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepo extends JpaRepository<DoctorAvailability,Integer> {
    Optional<DoctorAvailability> findByDoctorAndDate(Doctor doctor, LocalDate date);
    List<DoctorAvailability> findByDateAndAvailableTrue(LocalDate date);
}
