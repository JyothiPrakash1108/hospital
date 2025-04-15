package com.aims.hospital.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class ClinicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String procedureName;
    private LocalDate procedureDate;
    @Lob
    private byte[] procedureNotes;
    @Lob
    private byte[] dischargeSummary;
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    public ClinicalHistory() {
    }

    public ClinicalHistory(int id, String procedureName, LocalDate procedureDate, byte[] procedureNotes, byte[] dischargeSummary, Patient patient) {
        this.id = id;
        this.procedureName = procedureName;
        this.procedureDate = procedureDate;
        this.procedureNotes = procedureNotes;
        this.dischargeSummary = dischargeSummary;
        this.patient = patient;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public LocalDate getProcedureDate() {
        return procedureDate;
    }

    public void setProcedureDate(LocalDate procedureDate) {
        this.procedureDate = procedureDate;
    }

    public byte[] getProcedureNotes() {
        return procedureNotes;
    }

    public void setProcedureNotes(byte[] procedureNotes) {
        this.procedureNotes = procedureNotes;
    }

    public byte[] getDischargeSummary() {
        return dischargeSummary;
    }

    public void setDischargeSummary(byte[] dischargeSummary) {
        this.dischargeSummary = dischargeSummary;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
