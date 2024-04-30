package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.Patients;


@Repository
public interface PatientRepository extends JpaRepository<Patients, Integer> {
    
    Patients findByPatientId(int patientId);
    
    Patients findByAbhaAddress(String abhaAddress);

    Patients findByAbhaId(String abhaId);

}