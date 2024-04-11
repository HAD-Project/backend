package com.example.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Entities.Patients;
import com.example.backend.Models.abdm.GenerateLinkingTokenRes;
import com.example.backend.Repositories.PatientRepository;

@Service
public class CallbackServices {

    @Autowired
    PatientRepository patientRepository;

    public void onGenerateLinkingToken(GenerateLinkingTokenRes req) {
        Patients patient = patientRepository.findByAbhaAddress(req.getAbhaAddress());
        patient.setLinkToken(req.getLinkToken());
        patientRepository.save(patient);
    }
}
