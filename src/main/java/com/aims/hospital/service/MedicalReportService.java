package com.aims.hospital.service;

import com.aims.hospital.model.MedicalReport;
import com.aims.hospital.model.Patient;
import com.aims.hospital.repository.MedicalReportRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class MedicalReportService implements MedicalReportServiceInterface{
    @Autowired
    private MedicalReportRepo medicalReportRepo;
    @Override
    public void saveMedicalReport(MedicalReport medicalReport) {
        medicalReportRepo.save(medicalReport);
    }

    @Override
    public List<MedicalReport> getMedicalReport(Patient patient) {
        List<MedicalReport> reports = medicalReportRepo.findByPatient(patient);
        if (reports.isEmpty()){
            return Collections.emptyList();
        }
        return reports;
    }
}
