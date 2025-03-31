package com.aims.hospital.repository;

import com.aims.hospital.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Integer> {
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.localDateTime = :localDateTime")
    int countAppointmentsByDate(LocalDateTime localDateTime);
}
