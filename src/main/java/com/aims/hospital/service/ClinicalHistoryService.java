package com.aims.hospital.service;

import com.aims.hospital.model.ClinicalHistory;
import com.aims.hospital.model.Patient;
import com.aims.hospital.repository.ClinicalHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class ClinicalHistoryService implements ClinicalHistoryServiceInterface{
    @Autowired
    private ClinicalHistoryRepo clinicalHistoryRepo;

    @Override
    public void saveClinicalHistory(ClinicalHistory clinicalHistory) {
        clinicalHistoryRepo.save(clinicalHistory);
    }

    @Override
    public List<ClinicalHistory> getClinicalHistory(Patient patient) {
        List<ClinicalHistory> histories = clinicalHistoryRepo.findByPatient(patient);
        if(histories.isEmpty()){
            return Collections.emptyList();
        }
        return histories;
    }
}
