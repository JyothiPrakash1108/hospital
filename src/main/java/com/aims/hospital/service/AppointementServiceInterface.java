package com.aims.hospital.service;

import com.aims.hospital.enums.Status;
import com.aims.hospital.model.Appointment;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;
import org.springframework.cglib.core.Local;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointementServiceInterface {
    List<Appointment> getAppointmentsForDoctorToday(int doctorId);
    void updateAppointmentStatus(int appointmentId, Status status);
    List<Appointment> getAppointmentsByPatientId(int patientId);
    int countUpcomingAppointments(int patientId);
    int countPastAppointments(int patientId);
    void cancelAppointment(int appointmentId);
    List<LocalTime> getAvailableSlots(Doctor doctor, LocalDate localDate);
    public void bookApointment(Patient patient, Doctor doctor, LocalDate localDate,LocalTime localTime);
    List<Appointment> findAppointmentByDoctor(Doctor doctor);
}
