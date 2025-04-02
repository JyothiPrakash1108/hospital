package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Appointment;

import java.util.List;

public interface AppointementServiceInterface {
    List<Appointment> getAppointmentsForDoctorToday(int doctorId);
    void updateAppointmentStatus(int appointmentId, Status status);
    List<Appointment> getAppointmentsByPatientId(int patientId);
    int countUpcomingAppointments(int patientId);
    int countPastAppointments(int patientId);
    void cancelAppointment(int appointmentId);
}
