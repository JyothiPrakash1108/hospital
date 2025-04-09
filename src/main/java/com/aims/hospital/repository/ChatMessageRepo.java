package com.aims.hospital.repository;

import com.aims.hospital.model.ChatMessage;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepo extends JpaRepository<ChatMessage,Integer> {
    List<ChatMessage> findByDoctorAndPatientOrderByTimestampAsc(Doctor doctor, Patient patient);
}
