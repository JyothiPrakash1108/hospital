package com.aims.hospital.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] fileData;
    private String fileType;
    @OneToOne
    @JoinColumn(name = "appointment_id",referencedColumnName = "id")
    @JsonIgnore
    private Appointment appointment;
    public Prescription(){

    }

    public Prescription(int id, String description, byte[] fileData, Appointment appointment,String fileType) {
        this.id = id;
        this.description = description;
        this.fileData = fileData;
        this.appointment = appointment;
        this.fileType = fileType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
