package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Services.ABDMServices_Shrutik;
import com.example.backend.Services.DoctorService;

import reactor.core.publisher.Mono;

import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Records;
import com.example.backend.Models.RecordModel;
import com.example.backend.Models.abdm.auth.patient.PatientAuthOnInitReq;
import com.example.backend.Models.abdm.auth.patient.PatienthAuthOnInitRes;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    ABDMServices_Shrutik abdmServices;
    
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
            doctorService.createRecord(record);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getRecords")
    public ResponseEntity<List<RecordModel>> getMethodName(@RequestParam Integer patientId) {
        try {
            List<RecordModel> records = doctorService.getRecords(patientId);
            return ResponseEntity.ok().body(records);
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/patientAuthInit")
    public Mono<ResponseEntity<PatienthAuthOnInitRes>> patientAuthInit(@RequestBody PatientAuthOnInitReq body) throws Exception {
        return abdmServices.initPatientAuth(body.getAuthMethod(), body.getHealthid())
            .map(ResponseEntity::ok)
            .onErrorResume(e -> {
                PatienthAuthOnInitRes errorRes = new PatienthAuthOnInitRes();
                errorRes.setTxnId("-1");
                return Mono.just(ResponseEntity.internalServerError().body(errorRes));
            });
    }

}
