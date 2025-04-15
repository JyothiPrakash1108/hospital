package com.aims.hospital.service;

import com.aims.hospital.model.MedicalReport;
import com.aims.hospital.model.Patient;

import java.util.List;

public interface MedicalReportServiceInterface {
    void saveMedicalReport(MedicalReport medicalReport);
    List<MedicalReport> getMedicalReport(Patient patient);
}
