package com.aims.hospital.configuration;

import com.aims.hospital.enums.Role;
import com.aims.hospital.model.Admin;
import com.aims.hospital.model.Doctor;
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
    }
    private void seedAdmin(){
        User user = new Admin();
        user.setUsername("ammu");
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
    }
}
