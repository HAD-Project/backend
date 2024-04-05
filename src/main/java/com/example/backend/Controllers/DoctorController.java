package com.example.backend.Controllers;

import com.example.backend.Entities.Departments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Models.DoctorModel;
import com.example.backend.Repositories.DepartmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
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
    public ResponseEntity<List<DoctorModel>> getDoctors() {
        try {
            List<Doctors> doctors = doctorRepository.findAll();
            List<DoctorModel> viewDoctors = new ArrayList<>();
            for(Doctors doctor: doctors){
                DoctorModel doctorModel = new DoctorModel();
                doctorModel.setName(doctor.getUser().getName());
                doctorModel.setEmail(doctor.getUser().getEmail());
                doctorModel.setGender(doctor.getUser().getGender());
                doctorModel.setQualifications(doctor.getQualifications());
                doctorModel.setDepartment(doctor.getDepartment().getName());
                viewDoctors.add(doctorModel);
            }
            return ResponseEntity.of(Optional.of(viewDoctors));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/viewDoctor/{id}")
    public ResponseEntity<Optional<DoctorModel>> getDoctor(@PathVariable int id) {
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
}
