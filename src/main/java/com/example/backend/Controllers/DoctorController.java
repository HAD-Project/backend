package com.example.backend.Controllers;

import com.example.backend.Entities.Departments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Records;
import com.example.backend.Models.DoctorModel;
import com.example.backend.Models.RecordModel;
import com.example.backend.Repositories.DepartmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Services.ABDMServices_Shrutik;
import com.example.backend.Services.DoctorService;
import org.springframework.web.bind.annotation.*;
import com.example.backend.Models.abdm.auth.patient.PatientAuthOnInitReq;
import com.example.backend.Models.abdm.auth.patient.PatienthAuthOnInitRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.backend.Entities.Role.DOCTOR;


@CrossOrigin
@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    ABDMServices_Shrutik abdmServices;

    @Autowired
    private DoctorService doctorService;
    @PostMapping("/createRecord")
    public ResponseEntity<Records> createRecord(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody RecordModel toAdd) {
        try {
            Records record = doctorService.createRecord(token.split(" ")[1], toAdd);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/viewDoctors")
    public ResponseEntity<List<DoctorModel>> getDoctors() {
        try {
            List<Doctors> doctors = doctorRepository.findAllByUserActiveTrue();
            List<DoctorModel> viewDoctors = new ArrayList<>();
            for(Doctors doctor: doctors){
                DoctorModel doctorModel = new DoctorModel();
                doctorModel.setName(doctor.getUser().getName());
                doctorModel.setEmail(doctor.getUser().getEmail());
                doctorModel.setGender(doctor.getUser().getGender());
                doctorModel.setQualifications(doctor.getQualifications());
                doctorModel.setDepartment(doctor.getDepartment().getName());
                doctorModel.setPhone(doctor.getUser().getPhone());
                viewDoctors.add(doctorModel);
            }
            return ResponseEntity.of(Optional.of(viewDoctors));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @GetMapping("/viewDoctor/{email}")
    public ResponseEntity<Optional<DoctorModel>> getDoctor(@PathVariable String email) {
        try {
            Optional<Doctors> doctors = doctorRepository.findByUserEmailAndUserActiveTrue(email);

            Doctors doctor = doctors.get();
            DoctorModel doctorModel = new DoctorModel();
            doctorModel.setName(doctor.getUser().getName());
            doctorModel.setEmail(doctor.getUser().getEmail());
            doctorModel.setGender(doctor.getUser().getGender());
            doctorModel.setQualifications(doctor.getQualifications());
            doctorModel.setDepartment(doctor.getDepartment().getName());
            doctorModel.setPhone(doctor.getUser().getPhone());
            Optional<DoctorModel> viewDoctor = Optional.of(doctorModel);
            return ResponseEntity.of(Optional.of(viewDoctor));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PostMapping("/createDoctor")
    public ResponseEntity<String> createDoctor(@RequestBody DoctorModel doctorModel) {
        try {
            Departments department = departmentRepository.findByName(doctorModel.getDepartment());
            if(department==null)
                return ResponseEntity.ok("Department doesn't Exists");
            Doctors doctor = new Doctors();
            doctor.getUser().setName(doctorModel.getName());
            doctor.getUser().setEmail(doctorModel.getEmail());
            doctor.getUser().setGender(doctorModel.getGender());
            doctor.getUser().setUsername(doctorModel.getUsername());
            doctor.getUser().setPhone(doctorModel.getPhone());
            doctor.getUser().setRole(DOCTOR);
            doctor.setQualifications(doctorModel.getQualifications());
            doctor.setDepartment(department);
            doctorRepository.save(doctor);
            return ResponseEntity.ok("Succesfully created");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PutMapping("/updateDoctor/{email}")
    public ResponseEntity<String> updateDoctor(@PathVariable String email,@RequestBody DoctorModel doctorModel) {
        try {
            Optional<Doctors> doctor = doctorRepository.findByUserEmailAndUserActiveTrue(email);
            if(doctor.isEmpty())
                return ResponseEntity.ok("Doctor doesn't Exists");
            Departments department = departmentRepository.findByName(doctorModel.getDepartment());
            if(department==null)
                return ResponseEntity.ok("Department doesn't Exists");
            Doctors doctorToBeUpdated = doctor.get();
            doctorToBeUpdated.getUser().setName(doctorModel.getName());
            doctorToBeUpdated.getUser().setEmail(doctorModel.getEmail());
            doctorToBeUpdated.getUser().setGender(doctorModel.getGender());
            doctorToBeUpdated.setQualifications(doctorModel.getQualifications());
            doctorToBeUpdated.getUser().setPhone(doctorModel.getPhone());
            doctorToBeUpdated.setDepartment(department);
            doctorRepository.save(doctorToBeUpdated);
            return ResponseEntity.ok("Succesfully Updated");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @DeleteMapping("/deleteDoctor/{email}")
    public ResponseEntity<String> deleteDoctor(@PathVariable String email) {
        try{
            Optional<Doctors> doctor = doctorRepository.findByUserEmailAndUserActiveTrue(email);
            if(doctor.isEmpty())
                return ResponseEntity.ok("Doctor doesn't Exists");
            doctor.get().getUser().setActive(false);
            doctorRepository.save(doctor.get());
            return ResponseEntity.ok("Succesfully Deleted");
        }
        catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }

//     @PostMapping("/patientAuthInit")
//     public Mono<ResponseEntity<PatienthAuthOnInitRes>> patientAuthInit(@RequestBody PatientAuthOnInitReq body) throws Exception {
//         return abdmServices.initPatientAuth(body.getAuthMethod(), body.getHealthid())
//             .map(ResponseEntity::ok)
//             .onErrorResume(e -> {
//                 PatienthAuthOnInitRes errorRes = new PatienthAuthOnInitRes();
//                 errorRes.setTxnId("-1");
//                 return Mono.just(ResponseEntity.internalServerError().body(errorRes));
//             });
//     }

    @GetMapping("/getPatients")
    public ResponseEntity<List<Patients>> getPatients(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        try {
            System.out.println("Getting patients");
            List<Patients> patients = doctorService.getPatients(token.split(" ")[1]);
            if(patients == null) {
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.ok().body(patients);
        }
        catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/patient")
    public ResponseEntity<Patients> getOnePatient(@RequestParam int patientId) {
        try {
            Patients patient = doctorService.getPatient(patientId);
            return ResponseEntity.ok().body(patient);
        }
        catch (Exception e) {
            System.out.println("Error in getting one patient: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/getRecords")
    public ResponseEntity<List<RecordModel>> getRecords(@RequestParam int patientId) {
        try {
            List<RecordModel> records = doctorService.getRecords(patientId);
            return ResponseEntity.ok().body(records);
        }
        catch (Exception e) {
            System.out.println("Error in getting patient records: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
