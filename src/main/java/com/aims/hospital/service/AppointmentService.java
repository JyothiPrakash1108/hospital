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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Override
    public List<Appointment> filterAppointments(Doctor doctor, String patientName, LocalDate fromDate, LocalDate toDate) {
        List<Appointment> appointments = appointmentRepo.findByDoctor(doctor);
        return appointments.stream()
                .filter(app -> {
                    boolean nameMatch = (patientName == null || patientName.isBlank())
                            || app.getPatient().getUsername().toLowerCase().contains(patientName.toLowerCase());

                    boolean dateMatch = true;
                    if (fromDate != null && toDate != null) {
                        LocalDate date = app.getLocalDateTime().toLocalDate();
                        dateMatch = (date.isEqual(fromDate) || date.isAfter(fromDate)) &&
                                (date.isEqual(toDate) || date.isBefore(toDate));
                    } else if (fromDate != null) {
                        LocalDate date = app.getLocalDateTime().toLocalDate();
                        dateMatch = date.isEqual(fromDate) || date.isAfter(fromDate);
                    } else if (toDate != null) {
                        LocalDate date = app.getLocalDateTime().toLocalDate();
                        dateMatch = date.isEqual(toDate) || date.isBefore(toDate);
                    }

                    return nameMatch && dateMatch;
                })
                .sorted(Comparator.comparing(Appointment::getLocalDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Appointment findById(int appointmentId) {
        return appointmentRepo.findById(appointmentId).orElse(null);
    }

    @Override
    public Set<Patient> getVisitedPatientsByDoctor(Doctor doctor) {
        List<Appointment>  appointments = appointmentRepo.findByDoctor(doctor);
        return appointments.stream()
                .map(Appointment :: getPatient)
                .collect(Collectors.toSet());
    }

    @Override
    public List<Doctor> findDoctorsWithCompletedAppointments(Patient patient) {
        return appointmentRepo.findDoctorsByPatientAndStatus(patient.getId(), Status.COMPLETED);
    }

}
