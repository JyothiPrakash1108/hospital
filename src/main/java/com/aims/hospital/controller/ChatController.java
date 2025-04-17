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


      /*  @MessageMapping("/chat")
        public void handleChat(ChatMessage incomingMessage, Principal principal) {
            String email = principal.getName();

            // Determine who is the sender (patient or doctor)
            Patient patient = patientService.findPatientByEmail(email);
            Doctor doctor = doctorService.findByEmail(email);

            if (patient != null) {
                // Ensure incomingMessage has a doctor to send the message to
                if (incomingMessage.getDoctor() != null) {
                    int doctorId = incomingMessage.getDoctor().getId();
                    Doctor receiverDoctor = doctorService.findById(incomingMessage.getDoctor().getId());
                    if (receiverDoctor != null) {
                        // Save message with the patient and doctor
                        ChatMessage saved = chatService.saveMessage(incomingMessage.getMessage(), patient, receiverDoctor, "PATIENT");
                        messagingTemplate.convertAndSendToUser(
                                String.valueOf(receiverDoctor.getEmail()), "/queue/messages", saved);
                    } else {
                        // Handle case where doctor is not found
                        throw new IllegalArgumentException("Doctor not found");
                    }
                } else {
                    throw new IllegalArgumentException("Doctor information is missing in the message");
                }
            } else if (doctor != null) {
                // Ensure incomingMessage has a patient to send the message to
                if (incomingMessage.getPatient() != null) {
                    Patient receiverPatient = patientService.findById(incomingMessage.getPatient().getId());
                    if (receiverPatient != null) {
                        // Save message with the doctor and patient
                        ChatMessage saved = chatService.saveMessage(incomingMessage.getMessage(), receiverPatient, doctor, "DOCTOR");
                        messagingTemplate.convertAndSendToUser(
                                String.valueOf(receiverPatient.getEmail()), "/queue/messages", saved);
                    } else {
                        // Handle case where patient is not found
                        throw new IllegalArgumentException("Patient not found");
                    }
                } else {
                    throw new IllegalArgumentException("Patient information is missing in the message");
                }
            } else {
                throw new IllegalArgumentException("Neither patient nor doctor found");
            }
        }*/
      @MessageMapping("/chat")
      public void handleChat(ChatMessage incomingMessage, Principal principal) {
          String email = principal.getName();

          // Find sender (doctor or patient)
          Patient patient = patientService.findPatientByEmail(email);
          Doctor doctor = doctorService.findByEmail(email);

          if (patient != null) {
              // Patient sending to doctor
              Doctor receiverDoctor = doctorService.findById(incomingMessage.getDoctor().getId());
              if (receiverDoctor == null) {
                  throw new IllegalArgumentException("Doctor not found");
              }
              ChatMessage saved = chatService.saveMessage(
                      incomingMessage.getContent(),
                      patient,
                      receiverDoctor,
                      "PATIENT"
              );
              messagingTemplate.convertAndSendToUser(
                      String.valueOf(receiverDoctor.getId()),
                      "/queue/messages",
                      saved
              );
          } else if (doctor != null) {
              // Doctor sending to patient
              Patient receiverPatient = patientService.findById(incomingMessage.getPatient().getId());
              if (receiverPatient == null) {
                  throw new IllegalArgumentException("Patient not found");
              }
              ChatMessage saved = chatService.saveMessage(
                      incomingMessage.getContent(),
                      receiverPatient,
                      doctor,
                      "DOCTOR"
              );
              messagingTemplate.convertAndSendToUser(
                      String.valueOf(receiverPatient.getId()),
                      "/queue/messages",
                      saved
              );
          } else {
              throw new IllegalArgumentException("Unauthorized user");
          }
      }


       /* @ResponseBody
        @GetMapping("/chat/messages")
        public List<ChatMessage> getMessages(@RequestParam int doctorId,
                                             @AuthenticationPrincipal(expression = "username") String username) {
            Patient patient = patientService.findPatientByEmail(username);
            Doctor doctor = doctorService.findById(doctorId);
            return (patient != null && doctor != null)
                    ? chatService.getChatHistory(patient, doctor)
                    : List.of();
        }*/
       @GetMapping("/chat/messages")
       @ResponseBody
       public List<ChatMessage> getMessages(
               @RequestParam(name = "selectedDoctorId",required = false) Integer doctorId,
               @RequestParam(name = "selectedPatientId",required = false) Integer patientId,
               Principal principal) {

            String username = principal.getName();
            System.out.println("doctor id:"+doctorId+"patient Id:"+patientId);
           if (doctorId != null) {
               Patient patient = patientService.findPatientByEmail(username);
               Doctor doctor = doctorService.findById(doctorId);
               return chatService.getChatHistory(patient, doctor);
           } else if (patientId != null) {
               // Doctor requesting messages
               Doctor doctor = doctorService.findByEmail(username);
               Patient patient = patientService.findById(patientId);
               return chatService.getChatHistory(patient, doctor);
           }
           throw new IllegalArgumentException("Must specify either doctorId or patientId");
       }
    }
