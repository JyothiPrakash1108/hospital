package com.aims.hospital.repository;

import com.aims.hospital.model.ClinicalHistory;
import com.aims.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClinicalHistoryRepo extends JpaRepository<ClinicalHistory,Integer> {
    List<ClinicalHistory> findByPatient(Patient patient);
}
