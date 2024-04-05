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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
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
            Optional<Doctors> doctors = doctorRepository.findById(id);

            Doctors doctor = doctors.get();
            DoctorModel doctorModel = new DoctorModel();
            doctorModel.setName(doctor.getUser().getName());
            doctorModel.setEmail(doctor.getUser().getEmail());
            doctorModel.setGender(doctor.getUser().getGender());
            doctorModel.setQualifications(doctor.getQualifications());
            doctorModel.setDepartment(doctor.getDepartment().getName());

            Optional<DoctorModel> viewDoctor = Optional.of(doctorModel);
            return ResponseEntity.of(Optional.of(viewDoctor));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PostMapping("/updateDoctor")
    public ResponseEntity<String> updateDoctor(@RequestBody DoctorModel doctorModel) {
        try {
            Optional<Doctors> doctor = doctorRepository.findByUserEmail(doctorModel.getEmail());
            Departments department = departmentRepository.findByName(doctorModel.getDepartment());
            Doctors doctorToBeUpdated = doctor.get();
            doctorToBeUpdated.getUser().setName(doctorModel.getName());
            doctorToBeUpdated.getUser().setEmail(doctorModel.getEmail());
            doctorToBeUpdated.getUser().setGender(doctorModel.getGender());
            doctorToBeUpdated.setQualifications(doctorModel.getQualifications());
            doctorToBeUpdated.setDepartment(department);
            return ResponseEntity.ok("Succesfully Updated");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
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
