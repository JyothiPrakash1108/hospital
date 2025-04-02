package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Appointment;
import com.aims.hospital.repository.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService implements AppointementServiceInterface{
    @Autowired
    private AppointmentRepo appointmentRepo;

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

}
