package com.aims.hospital.repository;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailabilityRepo extends JpaRepository<DoctorAvailability,Integer> {
    Optional<DoctorAvailability> findByDoctorAndDate(Doctor doctor, LocalDate date);
    List<DoctorAvailability> findByDateAndAvailableTrue(LocalDate date);
    @Query("SELECT d FROM DoctorAvailability d WHERE d.doctor = :doctor AND d.date BETWEEN :startDate AND :endDate")
    List<DoctorAvailability> findByDoctorAndDateBetween(
            @Param("doctor") Doctor doctor,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    boolean existsByDoctorAndDate(Doctor doctor, LocalDate date);
    List<DoctorAvailability> findByDoctorAndDateIn(Doctor doctor, List<LocalDate> dates);

}
