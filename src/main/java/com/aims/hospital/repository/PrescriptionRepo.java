package com.aims.hospital.repository;

import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescription,Integer> {
    Prescription findByAppointment(Appointment appointment);
}
