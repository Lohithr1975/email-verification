package com.project.verification;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Transactional
public class VerificationService {

    @Autowired
    private VerificationCodeRepository codeRepository;

    @Autowired
    private EmailService emailService;

    public String sendVerificationCode(String email) {
        // Generate 6-digit code
        String code = String.format("%06d", new Random().nextInt(999999));

        VerificationCode verification = new VerificationCode();
        verification.setEmail(email);
        verification.setCode(code);
        verification.setExpiryTime(LocalDateTime.now().plusMinutes(1));

        // Remove old and save new
        codeRepository.deleteByEmail(email);
        codeRepository.save(verification);

        // Send email
        emailService.sendEmail(email, "Your OTP Code", "Your verification code is: " + code);

        return "OTP sent to email";
    }

    public String verifyCode(String email, String inputCode) {
        Optional<VerificationCode> optional = codeRepository.findByEmail(email);

        if (optional.isEmpty()) {
            return "No OTP found for this email";
        }

        VerificationCode savedCode = optional.get();

        if (savedCode.getExpiryTime().isBefore(LocalDateTime.now())) {
            return "OTP expired. Please request a new one.";
        }


        if (savedCode.getCode().equals(inputCode)) {
            codeRepository.deleteByEmail(email);
            return "Email verified successfully!";
        } else {
            return "Invalid OTP. Please try again.";
        }
    }
}
