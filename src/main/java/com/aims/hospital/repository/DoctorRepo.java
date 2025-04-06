package com.aims.hospital.repository;

import com.aims.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Integer> {
    List<Doctor> findByIsAvailableTrue();
    Doctor findByEmail(String email);
    List<Doctor> findByDepartmentAndIsAvailableTrue(String department);
}
