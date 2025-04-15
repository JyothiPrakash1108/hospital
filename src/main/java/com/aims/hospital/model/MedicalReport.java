package com.aims.hospital.model;

import jakarta.persistence.*;

@Entity
public class MedicalReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Lob
    private byte[] report;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public MedicalReport() {
    }

    public MedicalReport(int id, String name, byte[] report, Patient patient) {
        this.id = id;
        this.name = name;
        this.report = report;
        this.patient = patient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getReport() {
        return report;
    }

    public void setReport(byte[] report) {
        this.report = report;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
