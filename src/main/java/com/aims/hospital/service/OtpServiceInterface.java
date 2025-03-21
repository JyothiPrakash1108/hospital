package com.aims.hospital.service;

import com.aims.hospital.model.OTP;

public interface OtpServiceInterface {
    String generateOtp();
    void saveOtp(String otp,String email);
    boolean validateOtp(String otp,String email);
    OTP fetchByEmail(String email);
    void deleteRecord(OTP otp);
}
