package com.aims.hospital.service;

import com.aims.hospital.model.ChatMessage;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;

import java.util.List;

public interface ChatServiceInterface {
    public ChatMessage saveMessage(String content, Patient patient, Doctor doctor, String sender);
    public List<ChatMessage> getChatHistory(Patient patient, Doctor doctor);
    }
