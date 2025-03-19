package com.aims.hospital.service;

public interface OtpServiceInterface {
    String generateOtp();
    void saveOtp(String otp,String email);
    boolean validateOtp(String otp,String email);
}
