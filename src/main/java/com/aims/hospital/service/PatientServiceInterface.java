package com.aims.hospital.service;

import com.aims.hospital.model.Patient;


public interface PatientServiceInterface {
    Patient addPatient(Patient patient);
    Patient findPatientByEmail(String email);
    void verifyPatient(Patient patient);
}
