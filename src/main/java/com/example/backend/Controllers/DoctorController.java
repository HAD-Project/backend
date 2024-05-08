package com.example.backend.Controllers;

import com.example.backend.Entities.Departments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Records;
import com.example.backend.Entities.Users;
import com.example.backend.Models.DoctorModel;
import com.example.backend.Models.FileUpload;
import com.example.backend.Models.PatientDetailsModel;
import com.example.backend.Models.RecordModel;
import com.example.backend.Models.frontend.RequestRecords;
import com.example.backend.Repositories.DepartmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Repositories.UserRepository;
import com.example.backend.Services.ABDMServices;
import com.example.backend.Services.DoctorService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

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
    private ABDMServices abdmServices;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/createRecord")
    public ResponseEntity<Records> createRecord(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam String txnId, @RequestBody RecordModel toAdd) {
        try {
            doctorService.createRecord(token.split(" ")[1], toAdd, txnId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println("Error in creating record: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFile(@RequestParam String txnId, @ModelAttribute FileUpload req) {
        doctorService.uploadFile(txnId, req);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String fileId, HttpServletRequest req, HttpServletResponse res) {
        return doctorService.downloadFile(fileId, res);
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
                doctorModel.setUsername(doctor.getUser().getUsername());
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
            doctorModel.setUsername(doctor.getUser().getUsername());
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

            Users user = new Users();
            user.setName(doctorModel.getName());
            user.setName(doctorModel.getName());
            user.setEmail(doctorModel.getEmail());
            user.setGender(doctorModel.getGender());
            user.setUsername(doctorModel.getUsername());
            user.setPhone(doctorModel.getPhone());
            user.setRole(DOCTOR);
            user.setPassword(doctorModel.getPassword());

            user = userRepository.save(user);
            
            Doctors doctor = new Doctors();
            doctor.setUser(user);
            doctor.setQualifications(doctorModel.getQualifications());
            doctor.setDepartment(department);
            doctorRepository.save(doctor);
            return ResponseEntity.ok("Successfully created");
        } catch (Exception e) {
            System.out.println("Error in DoctorController->createDoctor: " + e.getLocalizedMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }


    @PutMapping("/updateDoctor/{email}")
    public ResponseEntity<String> updateDoctor(@PathVariable String email, @RequestBody DoctorModel doctorModel) {
        try {
            // Check if the provided email is unique
            Optional<Doctors> optionalDoctorWithSameEmail = doctorRepository.findByUserEmailAndUserActiveTrue(email);
            if (optionalDoctorWithSameEmail.isEmpty()) {
                return ResponseEntity.ok("Doctor with the provided email doesn't exist");
            }

            Doctors existingDoctor = optionalDoctorWithSameEmail.get();

            // If the provided email is different from the existing one, check uniqueness
            if (!email.equals(doctorModel.getEmail())) {
                Optional<Doctors> doctorWithEmail = doctorRepository.findByUserEmailAndUserActiveTrue(doctorModel.getEmail());
                if (doctorWithEmail.isPresent()) {
                    return ResponseEntity.ok("Email already exists for another doctor");
                }
            }

            Departments department = departmentRepository.findByName(doctorModel.getDepartment());
            if (department == null) {
                return ResponseEntity.ok("Department doesn't exist");
            }
            existingDoctor.getUser().setName(doctorModel.getName() == null ? existingDoctor.getUser().getName() : doctorModel.getName());
            existingDoctor.getUser().setEmail(doctorModel.getEmail() == null ? existingDoctor.getUser().getEmail() : doctorModel.getEmail());
            existingDoctor.getUser().setGender(doctorModel.getGender() == null ? existingDoctor.getUser().getGender() : doctorModel.getGender());
            existingDoctor.getUser().setPhone(doctorModel.getPhone() == null ? existingDoctor.getUser().getPhone() : doctorModel.getPhone());
            existingDoctor.getUser().setUsername(doctorModel.getUsername() == null ? existingDoctor.getUser().getUsername() : doctorModel.getUsername());
            existingDoctor.setQualifications(doctorModel.getQualifications());
            existingDoctor.setDepartment(department);


            doctorRepository.save(existingDoctor);
            return ResponseEntity.ok("Successfully updated");
        } catch (Exception e) {
            System.out.println(e);
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
    public ResponseEntity<PatientDetailsModel> getOnePatient(@RequestParam int patientId) {
        try {
            PatientDetailsModel patient = doctorService.getPatient(patientId);
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

    @PostMapping("/generateLinkToken")
    public Mono<ResponseEntity<Object>> generateLinkToken(@RequestParam String abhaAddress) {
        try {
            return abdmServices.generateLinkingToken(abhaAddress)
                .then(Mono.just(ResponseEntity.ok().build()))
                .onErrorResume(e -> {
                    System.out.println("Error in creating linking token: " + e.getMessage());
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
        }
        catch (Exception e) {
            System.out.println("Error in creating linking token: " + e.getLocalizedMessage());
            return Mono.just(ResponseEntity.internalServerError().build());
        }
    }

    @PostMapping("/requestRecords")
    public ResponseEntity<Void> requestRecords(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody RequestRecords req) {
        try {
            doctorService.requestRecords(token.split(" ")[1], req);
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            System.out.println("Error in DoctorController:requestRecords: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

}
