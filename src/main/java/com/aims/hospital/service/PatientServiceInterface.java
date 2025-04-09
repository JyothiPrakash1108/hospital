package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Patient;


public interface PatientServiceInterface {
    Patient addPatient(Patient patient);
    Patient findPatientByEmail(String email);
    Patient findById(int id);
    void verifyPatient(Patient patient);
}
