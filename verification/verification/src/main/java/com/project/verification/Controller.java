package com.project.verification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class Controller {

    @Autowired
    private EmailService emailService;

    @GetMapping("login")
    public String login() {
        return "welcome";
    }



    @Autowired
    private VerificationService verificationService;

    @PostMapping("send")
    public ResponseEntity<String> sendOTP(@RequestParam String email) {
        String message = verificationService.sendVerificationCode(email);
        return ResponseEntity.ok(message);
    }

    @PostMapping("check")
    public ResponseEntity<String> checkOTP(@RequestParam String email, @RequestParam String code) {
        String message = verificationService.verifyCode(email, code);
        return ResponseEntity.ok(message);
    }

}
