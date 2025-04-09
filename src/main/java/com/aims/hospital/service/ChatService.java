package com.aims.hospital.service;

import com.aims.hospital.model.ChatMessage;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;
import com.aims.hospital.repository.ChatMessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService implements ChatServiceInterface{
    @Autowired
    private ChatMessageRepo chatMessageRepo;
    @Override
    public ChatMessage saveMessage(String content, Patient patient, Doctor doctor, String sender) {
        ChatMessage msg = new ChatMessage();
                msg.setMessage(content);
                msg.setDoctor(doctor);
                msg.setPatient(patient);
                msg.setSender(sender);
                msg.setTimestamp(LocalDateTime.now());
        return chatMessageRepo.save(msg);
    }

    @Override
    public List<ChatMessage> getChatHistory(Patient patient, Doctor doctor) {
        return chatMessageRepo.findByDoctorAndPatientOrderByTimestampAsc(doctor, patient);
    }
}
