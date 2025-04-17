package com.aims.hospital.model;

import jakarta.persistence.*;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Builder
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String content;
    private String sender;
    private LocalDateTime timestamp;
    @ManyToOne(fetch = FetchType.EAGER)
    private Doctor doctor;
    @ManyToOne(fetch = FetchType.EAGER)
    private Patient patient;

    public ChatMessage() {
    }

    public ChatMessage(int id, String message, String sender, LocalDateTime timestamp, Doctor doctor, Patient patient) {
        this.id = id;
        this.content = message;
        this.sender = sender;
        this.timestamp = timestamp;
        this.doctor = doctor;
        this.patient = patient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
