package com.aims.hospital.repository;

import com.aims.hospital.model.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtpRepo extends JpaRepository<OTP,Integer> {
    OTP findByEmailAndOtp(String email,String otp);
    OTP findByEmail(String email);
}
