package com.example.backend.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.AccessTokenModel;
import com.example.backend.Models.AuthInit;
import com.example.backend.Models.ConfirmAuthModel;
import com.example.backend.Models.PatientProfileModel;
import com.example.backend.Models.ResendAuthModel;
import com.example.backend.Models.TxnIDModel;
import com.example.backend.Models.XTokenModel;
import com.example.backend.Services.ABDMServices;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/abdm")
public class ABDMController {

    @Autowired
    private ABDMServices abdmServices;
    
    @PostMapping("/initiateSession")
    public void abdmInitiateSession() {
        try {
            abdmServices.initiateSession();
            System.out.println("Initiation Successful!");
            System.out.println(abdmServices.getAccessToken());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/initAuth")
    public ResponseEntity<TxnIDModel> initializeAuth(@RequestBody AuthInit authInit) {
        try {
            System.out.println(authInit.getAuthMethod());
            System.out.println(authInit.getHealthid());
            TxnIDModel txnIDModel = abdmServices.initializeAuth(authInit);

            return ResponseEntity.of(Optional.of(txnIDModel));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<TxnIDModel> resendOtp(@RequestBody ResendAuthModel resendAuthModel) {
        try {
            TxnIDModel txnIDModel = abdmServices.resendAuth(resendAuthModel);

            return ResponseEntity.of(Optional.of(txnIDModel));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/confirmMobileOtp")
    public ResponseEntity<XTokenModel> confirmMobileOtp(@RequestBody ConfirmAuthModel confirmAuthModel) {
        try {
            XTokenModel xTokenModel = abdmServices.confirmAuth(confirmAuthModel, "MOBILE");

            return ResponseEntity.of(Optional.of(xTokenModel));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/confirmAadhaarOtp")
    public ResponseEntity<XTokenModel> confirmAadhaarOtp(@RequestBody ConfirmAuthModel confirmAuthModel) {
        try {
            XTokenModel xTokenModel = abdmServices.confirmAuth(confirmAuthModel, "AADHAAR");

            return ResponseEntity.of(Optional.of(xTokenModel));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/getProfile")
    public ResponseEntity<PatientProfileModel> getProfile() {
        try {
            PatientProfileModel patientProfileModel = abdmServices.fetchProfile();

            return ResponseEntity.of(Optional.of(patientProfileModel));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    
}
