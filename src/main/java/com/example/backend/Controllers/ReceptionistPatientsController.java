package com.example.backend.Controllers;


import com.example.backend.Entities.Patients;
import com.example.backend.Models.*;
import com.example.backend.Services.ReceptionistPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/receptionist/patients")
public class ReceptionistPatientsController {
    @Autowired
    private ReceptionistPatientService receptionistPatientService;

    @PostMapping("/register")
    public ResponseEntity<Patients> addPatient(@RequestBody ReceptionistPatientModel receptionistPatientModel) {
        System.out.println(receptionistPatientModel.toString());
        try {
            Patients patients = receptionistPatientService.registerPatient(receptionistPatientModel);

            return ResponseEntity.of(Optional.of(patients));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Patients>> getPatients() {
        try {
            List<Patients> patients = receptionistPatientService.getPatients();
            return ResponseEntity.of(Optional.of(patients));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/patient/view")
    public ResponseEntity<PatientInfoModel> getPatientData(@RequestParam Integer patientId) {
        try {
            PatientInfoModel patientInfo = receptionistPatientService.getPatientData(patientId);
            return ResponseEntity.of(Optional.of(patientInfo));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/patient/edit")
    public ResponseEntity<String> updatePatient(@RequestBody PatientUpdateModel patientUpdateModel) {
        try {
            String res = receptionistPatientService.updatePatient(patientUpdateModel);
            return ResponseEntity.of(Optional.of(res));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/patient/delete")
    public ResponseEntity<String> deletePatient(@RequestParam Integer patientId) {
        try {
            String res = receptionistPatientService.deletePatient(patientId);
            return ResponseEntity.of(Optional.of(res));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
