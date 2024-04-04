package com.example.backend.Controllers;


import com.example.backend.Entities.Patients;
import com.example.backend.Models.LoginModel;
import com.example.backend.Models.LoginResponseModel;
import com.example.backend.Models.ReceptionistPatientModel;
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

    @GetMapping("/getPatients")
    public List<Patients> getPatients() {
        try {
            return receptionistPatientService.getPatients();
        } catch (Exception e) {
            return null;
        }
    }
}
