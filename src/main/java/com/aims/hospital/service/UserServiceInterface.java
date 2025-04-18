package com.aims.hospital.service;

import com.aims.hospital.model.User;

public interface UserServiceInterface {
    void registerPatient(User user);
    void saveUser(User User);
    void registerAdmin(User user);
    void registerDoctor(User user);
}
