package com.example.backend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Entities.Patients;
import com.example.backend.Repositories.PatientRepository;

@Service
public class PatientService {
    
    @Autowired
    private PatientRepository patientRepository;

    Patients findPatientById(int patientId) {
        return patientRepository.findByPatientId(patientId);
    }
}
