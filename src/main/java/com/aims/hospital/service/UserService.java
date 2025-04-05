package com.aims.hospital.service;

import com.aims.hospital.enums.Role;
import com.aims.hospital.model.User;
import com.aims.hospital.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserServiceInterface{
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerPatient(User user){
        user.setRole(Role.PATIENT);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(false);
        userRepo.save(user);
    }

    @Override
    public void saveUser(User User) {
        userRepo.save(User);
    }

    @Override
    public void registerAdmin(User user) {
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(true);
        userRepo.save(user);
    }

    @Override
    public void registerDoctor(User user) {
        user.setRole(Role.DOCTOR);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setVerified(true);
        userRepo.save(user);
    }
}
