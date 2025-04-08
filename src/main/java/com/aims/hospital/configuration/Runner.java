package com.aims.hospital.configuration;

import com.aims.hospital.enums.Gender;
import com.aims.hospital.enums.Role;
import com.aims.hospital.model.Admin;
import com.aims.hospital.model.Doctor;
import com.aims.hospital.model.Patient;
import com.aims.hospital.model.User;
import com.aims.hospital.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {
    @Autowired
   private UserService userService;
    @Override
    public void run(String... args) throws Exception {
        seedAdmin();
        seedDoctor();
        seedPatients();
    }
    private void seedAdmin(){
        User user = new Admin();
        user.setUsername("a");
        user.setPassword("jyothiprakash");
        user.setRole(Role.ADMIN);
        user.setVerified(true);
        user.setEmail("ad@gmail.com");
        user.setPhoneNumber("8008977704");
        userService.registerAdmin(user);
    }
    private void seedDoctor(){
        Doctor doc1 = new Doctor();
        doc1.setUsername("Dr. John Doe");
        doc1.setEmail("john.doe@example.com");
        doc1.setPassword("doctor123");
        doc1.setDepartment("Cardiology");
        doc1.setPhoneNumber("9177467682");
        userService.registerDoctor(doc1);
        Doctor doc2 = new Doctor();
        doc2.setUsername("Dr. Jane Smith");
        doc2.setEmail("jane.smith@example.com");
        doc2.setPassword("doctor123");
        doc2.setRole(Role.DOCTOR);
        doc2.setDepartment("Neurology");
        doc2.setPhoneNumber("9177467682");
        doc2.setAvailable(true);
        doc2.setVerified(true);
        userService.registerDoctor(doc2);

        Doctor doc3 = new Doctor();
        doc3.setUsername("Dr. Anil Kumar");
        doc3.setEmail("anil.kumar@hospital.com");
        doc3.setPassword("password"); // store encrypted in real apps
        doc3.setDepartment("Cardiology");
        doc3.setAvailable(true);
        doc3.setPhoneNumber("1234567890");

        Doctor doc4 = new Doctor();
        doc4.setUsername("Dr. Priya Sharma");
        doc4.setEmail("priya.sharma@hospital.com");
        doc4.setPassword("password");
        doc4.setDepartment("Dermatology");
        doc4.setAvailable(true);
        doc4.setPhoneNumber("7418529630");

        Doctor doc5 = new Doctor();
        doc5.setPhoneNumber("3697852140");
        doc5.setUsername("Dr. Ramesh Babu");
        doc5.setEmail("ramesh.babu@hospital.com");
        doc5.setPassword("password");
        doc5.setDepartment("Orthopedics");
        doc5.setAvailable(true);

        userService.registerDoctor(doc3);
        userService.registerDoctor(doc4);
        userService.registerDoctor(doc5);
    }
    private void seedPatients(){
        Patient patient1 = new Patient();
        patient1.setUsername("jyothi");
        patient1.setEmail("jyothi1108@gmail.com");
        patient1.setPassword("jyothi1108@gmail.com");
        patient1.setAge(20);
        patient1.setGender(Gender.MALE);
        patient1.setPhoneNumber("7032061881");
        userService.registerPatient(patient1);
        patient1.setVerified(true);
        userService.saveUser(patient1);
    }
}
