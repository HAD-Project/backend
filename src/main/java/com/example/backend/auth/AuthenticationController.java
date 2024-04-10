package com.example.backend.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

    private final AuthenticationService service;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        try {
            return ResponseEntity.ok(service.register(request));
        } catch (DataIntegrityViolationException e) {
            // Log the exception
            System.out.println("Data integrity violation occurred during registration: " + e.getMessage());
            AuthenticationResponse errorResponse = new AuthenticationResponse();
            errorResponse.setMessage("Invalid data provided.");
            // Return a short error message for data integrity violation
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            System.out.println("Error occurred during registration: " + e.getMessage());
            AuthenticationResponse errorResponse = new AuthenticationResponse();
            errorResponse.setMessage("An error occurred during registration.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }




        @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request){
    try {
        return ResponseEntity.ok(service.authenticate(request));
    }
    catch (Exception e){
        AuthenticationResponse errorResponse = new AuthenticationResponse();
        errorResponse.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    }
}
