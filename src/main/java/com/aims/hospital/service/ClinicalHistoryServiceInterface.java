package com.aims.hospital.service;

import com.aims.hospital.model.ClinicalHistory;
import com.aims.hospital.model.Patient;

import java.util.List;

public interface ClinicalHistoryServiceInterface {
    void saveClinicalHistory(ClinicalHistory clinicalHistory);
    List<ClinicalHistory> getClinicalHistory(Patient patient);
}
