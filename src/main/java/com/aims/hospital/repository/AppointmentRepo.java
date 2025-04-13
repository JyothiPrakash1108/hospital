package com.aims.hospital.repository;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointment,Integer> {
    @Query("SELECT COUNT(a) FROM Appointment a WHERE a.localDateTime = :localDateTime")
    int countAppointmentsByDate(LocalDateTime localDateTime);
    @Query("SELECT a FROM Appointment a WHERE a.doctor.id = :doctorId AND a.localDateTime BETWEEN :startOfDay AND :endOfDay")
    List<Appointment> findByDoctorIdAndDate(@Param("doctorId") int doctorId, @Param("startOfDay") LocalDateTime startOfDay, @Param("endOfDay") LocalDateTime endOfDay);
    List<Appointment> findByPatientId(int patientId);
    int countByPatientIdAndStatus(int patientId, Status status);
    List<Appointment> findByDoctorAndLocalDateTimeBetween(Doctor doctor,LocalDateTime start,LocalDateTime end);
    boolean existsByDoctorAndLocalDateTime(Doctor doctor,LocalDateTime localDateTime);
    List<Appointment> findByDoctorOrderByLocalDateTimeAsc(Doctor doctor);


}
