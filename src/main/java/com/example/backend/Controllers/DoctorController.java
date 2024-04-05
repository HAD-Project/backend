// package com.example.backend.Controllers;

// import org.springframework.web.bind.annotation.RestController;

// import com.example.backend.Services.ABDMServices_Shrutik;
// import com.example.backend.Services.DoctorService;

// import reactor.core.publisher.Mono;

// import com.example.backend.Entities.Patients;
// import com.example.backend.Entities.Records;
// import com.example.backend.Models.RecordModel;
// import com.example.backend.Models.abdm.auth.patient.PatientAuthOnInitReq;
// import com.example.backend.Models.abdm.auth.patient.PatienthAuthOnInitRes;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;


// @CrossOrigin
// @RestController
// @RequestMapping("/api/v1/doctor")
// public class DoctorController {
//     @Autowired
//     private DoctorRepository doctorRepository;
//     @Autowired
//     private DepartmentRepository departmentRepository;
//     @Autowired
//     private UserRepository userRepository;

//     @GetMapping("/viewDoctors")
//     public ResponseEntity<List<DoctorModel>> getDoctors() {
//         try {
//             List<Doctors> doctors = doctorRepository.findAll();
//             List<DoctorModel> viewDoctors = new ArrayList<>();
//             for(Doctors doctor: doctors){
//                 DoctorModel doctorModel = new DoctorModel();
//                 doctorModel.setName(doctor.getUser().getName());
//                 doctorModel.setEmail(doctor.getUser().getEmail());
//                 doctorModel.setGender(doctor.getUser().getGender());
//                 doctorModel.setQualifications(doctor.getQualifications());
//                 doctorModel.setDepartment(doctor.getDepartment().getName());
//                 viewDoctors.add(doctorModel);
//             }
//             return ResponseEntity.of(Optional.of(viewDoctors));
//         } catch (Exception e) {
//             return ResponseEntity.status(500).build();
//         }
//     }
//     @GetMapping("/viewDoctor/{email}")
//     public ResponseEntity<Optional<DoctorModel>> getDoctor(@PathVariable String email) {
//         try {
//             Optional<Doctors> doctors = doctorRepository.findByUserEmail(email);

// //             Doctors doctor = doctors.get();
// //             DoctorModel doctorModel = new DoctorModel();
// //             doctorModel.setName(doctor.getUser().getName());
// //             doctorModel.setEmail(doctor.getUser().getEmail());
// //             doctorModel.setGender(doctor.getUser().getGender());
// //             doctorModel.setQualifications(doctor.getQualifications());
// //             doctorModel.setDepartment(doctor.getDepartment().getName());

//             Optional<DoctorModel> viewDoctor = Optional.of(doctorModel);
//             return ResponseEntity.of(Optional.of(viewDoctor));
//         } catch (Exception e) {
//             return ResponseEntity.status(500).build();
//         }
//     }
//     @PostMapping("/updateDoctor/{email}")
//     public ResponseEntity<String> updateDoctor(@PathVariable String email,@RequestBody DoctorModel doctorModel) {
//         try {
//             Optional<Doctors> doctor = doctorRepository.findByUserEmail(email);
//             if(doctor.isEmpty())
//                 return ResponseEntity.ok("Doctor doesn't Exists");
//             Departments department = departmentRepository.findByName(doctorModel.getDepartment());
//             Doctors doctorToBeUpdated = doctor.get();
//             doctorToBeUpdated.getUser().setName(doctorModel.getName());
//             doctorToBeUpdated.getUser().setEmail(doctorModel.getEmail());
//             doctorToBeUpdated.getUser().setGender(doctorModel.getGender());
//             doctorToBeUpdated.setQualifications(doctorModel.getQualifications());
//             doctorToBeUpdated.setDepartment(department);
//             doctorRepository.save(doctorToBeUpdated);
//             return ResponseEntity.ok("Succesfully Updated");
//         } catch (Exception e) {
//             return ResponseEntity.status(500).build();
//         }
//     }
// }
