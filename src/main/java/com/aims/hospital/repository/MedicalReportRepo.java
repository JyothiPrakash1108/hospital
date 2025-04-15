package com.aims.hospital.repository;

import com.aims.hospital.model.MedicalReport;
import com.aims.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalReportRepo extends JpaRepository<MedicalReport,Integer> {
    List<MedicalReport> findByPatient(Patient patient);
}
