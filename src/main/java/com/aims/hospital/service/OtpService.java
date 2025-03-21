package com.aims.hospital.service;

import com.aims.hospital.model.OTP;
import com.aims.hospital.repository.OtpRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional
public class OtpService implements OtpServiceInterface{

    private OtpRepo otpRepo;
    @Autowired
    public OtpService(OtpRepo otpRepo){
        this.otpRepo = otpRepo;
    }
    private final SecureRandom random = new SecureRandom();

    public String generateOtp(){
        return String.format("%06d", random.nextInt(1000000));
    }
    public void saveOtp(String otp,String email){
        OTP otp1 = new OTP();
        otp1.setOtp(otp);
        otp1.setEmail(email);
        otp1.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpRepo.save(otp1);
    }
    public boolean validateOtp(String otp,String email){
        OTP otp1 = otpRepo.findByEmailAndOtp(email,otp);
        if(otp!=null && otp1.getExpiryTime().isAfter(LocalDateTime.now())){
            otpRepo.delete(otp1);
            return true;
        }
        return false;
    }

    @Override
    public OTP fetchByEmail(String email) {
        return otpRepo.findByEmail(email);
    }

    @Override
    public void deleteRecord(OTP otp) {
         otpRepo.delete(otp);
    }

}
