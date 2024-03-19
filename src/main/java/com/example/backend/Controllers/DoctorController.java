package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Services.DoctorService;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Records;
import com.example.backend.Models.RecordModel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;
    
    @GetMapping("/overview")
    public String getOverview() {
        return new String();
    }

    @GetMapping("/appointments")
    public String getAppointments(@RequestParam Integer doctorId) {
        return new String();
    }
    
    @PostMapping("/request")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @GetMapping("/getPatients")
    public List<Patients> getPatients(@RequestParam Integer doctorId) {
        return doctorService.getPatients(doctorId);
    }

    @GetMapping("/patient")
    public Patients getPatient(@RequestParam Integer patientId) {
        return doctorService.getPatient(patientId);
    }

    @PostMapping("/createRecord")
    public ResponseEntity<Records> createRecord(@RequestBody RecordModel record) {
        try {
            Records createdRecord = doctorService.createRecord(record);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    
}
