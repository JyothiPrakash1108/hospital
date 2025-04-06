package com.aims.hospital.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@DiscriminatorValue("DOCTOR")
public class Doctor extends User{
    private String department;
    private boolean isAvailable= true;
    @OneToMany(mappedBy = "doctor",cascade = CascadeType.ALL)
    private List<Appointment> appointments;
    @OneToMany(mappedBy = "doctor" , cascade = CascadeType.ALL)
    private List<DoctorAvailability> doctorAvailabilities;

    public Doctor() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<DoctorAvailability> getDoctorAvailabilities() {
        return doctorAvailabilities;
    }

    public void setDoctorAvailabilities(List<DoctorAvailability> doctorAvailabilities) {
        this.doctorAvailabilities = doctorAvailabilities;
    }
}
