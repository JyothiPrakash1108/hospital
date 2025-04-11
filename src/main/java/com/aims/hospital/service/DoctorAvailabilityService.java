package com.aims.hospital.service;

import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import com.aims.hospital.repository.DoctorAvailabilityRepo;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class DoctorAvailabilityService  implements DoctorAvailabilityServiceInterface {
    @Autowired
    private DoctorAvailabilityRepo doctorAvailabilityRepo;
    @Autowired
    private EntityManager entityManager;

    @Override
    public DoctorAvailability getAvailableForDoctorOnDate(Doctor doctor, LocalDate localDate) {
        return doctorAvailabilityRepo.findByDoctorAndDate(doctor, localDate)
                .orElseGet(() -> {
                    DoctorAvailability availability = new DoctorAvailability();
                    availability.setDoctor(doctor);
                    availability.setDate(localDate);
                    return availability;
                });
    }

    @Override
    public List<DoctorAvailability> getUpcomingAvailability(Doctor doctor) {
        LocalDate today = LocalDate.now();
        LocalDate fourDaysLater = today.plusDays(3);
        return doctorAvailabilityRepo.findByDoctorAndDateBetween(doctor, today, fourDaysLater);
    }

    @Override
    public void saveAvailabilitySlots(Doctor doctor, List<DoctorAvailability> slots) {
        for (DoctorAvailability slot : slots) {
            slot.setDoctor(doctor);  // Very important!

            Optional<DoctorAvailability> existingOpt =
                    doctorAvailabilityRepo.findByDoctorAndDate(doctor, slot.getDate());

            if (existingOpt.isPresent()) {
                // Update existing slot
                DoctorAvailability existing = existingOpt.get();
                existing.setAvailable(slot.isAvailable());
                existing.setStartTime(slot.getStartTime());
                existing.setEndTime(slot.getEndTime());
                doctorAvailabilityRepo.save(existing);
            } else {
                // New slot
                doctorAvailabilityRepo.save(slot);
            }
        }
    }

    @Override
    public Optional<DoctorAvailability> findByDoctorAndDate(Doctor doctor, LocalDate date) {
        return doctorAvailabilityRepo.findByDoctorAndDate(doctor, date);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoctorAvailability> getAvailabilityForNext4Days(Doctor doctor) {
        /*
        LocalDate today = LocalDate.now();
        LocalDate fourDaysLater = today.plusDays(3);
        entityManager.clear();
        return doctorAvailabilityRepo.findByDoctorAndDateBetween(doctor, today, fourDaysLater);*/
        entityManager.clear();

        LocalDate today = LocalDate.now();
        LocalDate fourDaysLater = today.plusDays(3);

        // Fetch fresh from DB
        List<DoctorAvailability> availabilities = doctorAvailabilityRepo
                .findByDoctorAndDateBetween(doctor, today, fourDaysLater);

        // If no records exist, create default unavailable slots
        if (availabilities.isEmpty()) {
            return IntStream.range(0, 4)
                    .mapToObj(i -> new DoctorAvailability(
                            today.plusDays(i),
                            LocalTime.of(9, 0),
                            LocalTime.of(17, 0),
                            false,
                            doctor))
                    .collect(Collectors.toList());
        }

        return availabilities;

    }

    @Override
    @Transactional
    public void saveAll(List<DoctorAvailability> availabilities, Doctor doctor) {
        entityManager.clear();

        for (DoctorAvailability newAvailability : availabilities) {
            // Ensure doctor is always set
            newAvailability.setDoctor(doctor);

            // Find existing record (fresh from DB)
            Optional<DoctorAvailability> existingOpt = doctorAvailabilityRepo
                    .findByDoctorAndDate(doctor, newAvailability.getDate());

            if (existingOpt.isPresent()) {
                DoctorAvailability existing = existingOpt.get();
                // Update all fields including doctor
                existing.setStartTime(newAvailability.getStartTime());
                existing.setEndTime(newAvailability.getEndTime());
                existing.setAvailable(newAvailability.isAvailable());
                existing.setDoctor(doctor); // Ensure doctor is set

                // Save and refresh
                doctorAvailabilityRepo.save(existing);
                entityManager.flush();
                entityManager.refresh(existing);
            } else {
                // Save new with doctor association
                newAvailability.setDoctor(doctor);
                DoctorAvailability saved = doctorAvailabilityRepo.save(newAvailability);
                entityManager.flush();
                entityManager.refresh(saved);

            }
        }
    }
}
