package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.DoctorAvailability;
import com.aims.hospital.model.Patient;
import com.aims.hospital.repository.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService implements AppointementServiceInterface{
    @Autowired
    private AppointmentRepo appointmentRepo;
    @Autowired
    private DoctorAvailabilityService doctorAvailabilityService;

    @Override
    public List<Appointment> getAppointmentsForDoctorToday(int doctorId) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startOfDay = today.toLocalDate().atTime(0, 0, 0);
        LocalDateTime endOfDay = today.toLocalDate().atTime(23, 59, 59);
        return appointmentRepo.findByDoctorIdAndDate(doctorId,startOfDay,endOfDay);
    }

    @Override
    public void updateAppointmentStatus(int appointmentId, Status status) {
        Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(
                ()-> new RuntimeException("appointment not found")
        );
        appointment.setStatus(status);
        appointmentRepo.save(appointment);
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        return appointmentRepo.findByPatientId(patientId);
    }

    @Override
    public int countUpcomingAppointments(int patientId) {
        return appointmentRepo.countByPatientIdAndStatus(patientId,Status.PENDING);
    }

    @Override
    public int countPastAppointments(int patientId) {
        return appointmentRepo.countByPatientIdAndStatus(patientId,Status.COMPLETED);
    }

    @Override
    public void cancelAppointment(int patientId) {
        return ;
    }

    @Override
    public List<LocalTime> getAvailableSlots(Doctor doctor, LocalDate localDate) {
        DoctorAvailability availability = doctorAvailabilityService.getAvailableForDoctorOnDate(doctor,localDate);

        List<LocalTime> availableSlots = new ArrayList<>();

        if (!availability.isAvailable()) return availableSlots;

        LocalTime start = availability.getStartTime();
        LocalTime end = availability.getEndTime();

        while (start.isBefore(end)) {
            LocalDateTime slot = LocalDateTime.of(localDate, start);
            if (!appointmentRepo.existsByDoctorAndLocalDateTime(doctor, slot)) {
                availableSlots.add(start);
            }
            start = start.plusMinutes(30);
        }

        return availableSlots;
    }

    @Override
    public void bookApointment(Patient patient, Doctor doctor, LocalDate localDate, LocalTime localTime) {
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setLocalDateTime(LocalDateTime.of(localDate,localTime));
        appointment.setStatus(Status.PENDING);
        appointmentRepo.save(appointment);
    }

    @Override
    public List<Appointment> findAppointmentByDoctor(Doctor doctor) {
        return appointmentRepo.findByDoctorOrderByLocalDateTimeAsc(doctor);
    }


}
