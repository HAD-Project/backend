package com.example.backend.Controllers;

import com.example.backend.Entities.Doctors;
import com.example.backend.Models.DoctorModel;
import com.example.backend.Models.LoginModel;
import com.example.backend.Models.LoginResponseModel;
import com.example.backend.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseModel> loginUser(@RequestBody LoginModel loginModel) {
        System.out.println(loginModel.toString());
        try {
            LoginResponseModel loginResponseModel = authService.loginUser(loginModel);

            return ResponseEntity.of(Optional.of(loginResponseModel));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestBody DoctorModel doctorModel) {
        System.out.println(doctorModel.toString());
        try {
            return ResponseEntity.ok("Logged out");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
