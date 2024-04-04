package com.example.backend.Controllers;

import com.example.backend.Entities.Doctors;
import com.example.backend.Models.DoctorModel;
import com.example.backend.Repositories.DepartmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/viewDoctors")
    public ResponseEntity<List<Doctors>> getDoctors() {
        try {
            List<Doctors> doctors = doctorRepository.findAll();
            return ResponseEntity.of(Optional.of(doctors));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/viewDoctor/{id}")
    public ResponseEntity<Optional<Doctors>> getDoctor(@PathVariable int id) {
        try {
            Optional<Doctors> doctors = doctorRepository.findById(id);
            return ResponseEntity.of(Optional.of(doctors));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PostMapping("/updateDoctor/{id}")
    public ResponseEntity<Doctors> updateDoctor(@PathVariable int id,@RequestBody DoctorModel doctorModel) {
        try {
            
            return ResponseEntity.of(Optional.of(doctorToBeUpdated));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
