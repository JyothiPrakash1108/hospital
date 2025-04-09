package com.aims.hospital.controller;

import com.aims.hospital.model.ChatMessage;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;
import com.aims.hospital.service.ChatService;
import com.aims.hospital.service.DoctorService;
import com.aims.hospital.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatService chatService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private PatientService patientService;


    @MessageMapping("/chat")
    public void handleChat(ChatMessage incomingMessage, Principal principal) {
        String email = principal.getName();

        // Determine who is the sender (patient or doctor)
        Patient patient = patientService.findPatientByEmail(email);
        Doctor doctor = doctorService.findByEmail(email);

        if (patient != null) {
            Doctor receiverDoctor = doctorService.findById(incomingMessage.getDoctor().getId());
            if (receiverDoctor != null) {
                ChatMessage saved = chatService.saveMessage(incomingMessage.getMessage(), patient, receiverDoctor, "PATIENT");
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(receiverDoctor.getId()), "/queue/messages", saved);
            }
        } else if (doctor != null) {
            Patient receiverPatient = patientService.findById(incomingMessage.getPatient().getId());
            if (receiverPatient != null) {
                ChatMessage saved = chatService.saveMessage(incomingMessage.getMessage(), receiverPatient, doctor, "DOCTOR");
                messagingTemplate.convertAndSendToUser(
                        String.valueOf(receiverPatient.getId()), "/queue/messages", saved);
            }
        }
    }

    @ResponseBody
    @GetMapping("/chat/messages")
    public List<ChatMessage> getMessages(@RequestParam int doctorId,
                                         @AuthenticationPrincipal(expression = "username") String username) {
        Patient patient = patientService.findPatientByEmail(username);
        Doctor doctor = doctorService.findById(doctorId);
        return (patient != null && doctor != null)
                ? chatService.getChatHistory(patient, doctor)
                : List.of();
    }
}
