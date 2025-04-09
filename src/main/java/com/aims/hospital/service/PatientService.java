package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Patient;
import com.aims.hospital.repository.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientService implements PatientServiceInterface{
    private PatientRepo patientRepo;
    @Autowired
    public PatientService(PatientRepo patientRepo){
        this.patientRepo = patientRepo;
    }

    @Override
    public Patient addPatient(Patient patient) {
        return patientRepo.save(patient);
    }

    @Override
    public Patient findPatientByEmail(String email) {
        return patientRepo.findByEmail(email);
    }

    @Override
    public Patient findById(int id) {
        return patientRepo.findById(id).orElse(null);
    }

    @Override
    public void verifyPatient(Patient patient) {
        patient.setVerified(true);
        patientRepo.save(patient);
    }

}
