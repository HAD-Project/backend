package com.example.backend.Controllers;

import java.util.List;
import java.util.Optional;

import com.example.backend.Entities.Admins;
import com.example.backend.Repositories.DoctorRepository;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.Services.AdminServices;
import com.example.backend.Entities.Doctors;
import com.example.backend.Models.DoctorDeleteResponseModel;
import com.example.backend.Models.DoctorModel;
import org.springframework.web.bind.annotation.GetMapping;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {


//    private final AdminServices adminServices;
//
//    @Autowired
//    public AdminController(AdminServices adminServices) {
//        this.adminServices = adminServices;
//    }
//    @PostMapping("/createDoctor")
//    public ResponseEntity<Doctors> createDoctor(@RequestBody DoctorModel doctorModel) {
//        System.out.println(doctorModel.toString());
//        try {
//            Doctors newDoctor = adminServices.createDoctor(doctorModel);
//
//            return ResponseEntity.of(Optional.of(newDoctor));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }

//    @GetMapping("/getDoctors")
//    public ResponseEntity<List<Admins>> getDoctors() {
//        try {
//            List<Admins> doctors = adminServices.getDoctors();
//
//            return ResponseEntity.of(Optional.of(doctors));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }

//    @PostMapping("/updateDoctor")
//    public ResponseEntity<Doctors> updateDoctor(@RequestBody DoctorModel doctorModel) {
//        try {
//            Doctors doctorToBeUpdated = adminServices.updateDoctorGender(doctorModel.getUsername(), doctorModel.getGender());
//            doctorToBeUpdated = adminServices.updateDoctorName(doctorModel.getUsername(), doctorModel.getName());
//            doctorToBeUpdated = adminServices.updateDoctorPassword(doctorModel.getUsername(), doctorModel.getPassword());
//            doctorToBeUpdated = adminServices.updateDoctorQualifications(doctorModel.getUsername(), doctorModel.getQualifications());
//
//            return ResponseEntity.of(Optional.of(doctorToBeUpdated));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }
    
//    @DeleteMapping("/deleteDoctor")
//    public ResponseEntity<DoctorDeleteResponseModel> deleteDoctor(@RequestBody DoctorModel doctorModel) {
//        try {
//            DoctorDeleteResponseModel doctorDeleteResponseModel = adminServices.deleteDoctor(doctorModel.getUsername());
//            return ResponseEntity.of(Optional.of(doctorDeleteResponseModel));
//        } catch (Exception e) {
//            return ResponseEntity.status(500).build();
//        }
//    }

}
