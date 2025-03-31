package com.aims.hospital.service;

import com.aims.hospital.model.Doctor;

import java.util.List;

public interface AdminServiceInterface {
    public int getTotalPatientsToday();
    public int getAvailableDoctorsCount();
    List<Doctor> getAvailableDoctors();
}
