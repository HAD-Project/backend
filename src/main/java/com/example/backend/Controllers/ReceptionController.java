package com.example.backend.Controllers;

import com.example.backend.Entities.Departments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Receptionists;
import com.example.backend.Entities.Users;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.backend.Models.ReceptionistModel;
import com.example.backend.Repositories.ReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.backend.Entities.Role.DOCTOR;
import static com.example.backend.Entities.Role.RECEPTIONIST;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/reception")
public class ReceptionController {
    @Autowired
    private ReceptionRepository receptionRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/viewReceptionists")
    public ResponseEntity<List<ReceptionistModel>> getReceptionists() {
        try {
            List<Receptionists> receptionists = receptionRepository.findAllByUserActiveTrue();
            List<ReceptionistModel> viewReceptionist = new ArrayList<>();
            for(Receptionists receptionist: receptionists){
                ReceptionistModel receptionistModel = new ReceptionistModel();
                receptionistModel.setName(receptionist.getUser().getName());
                receptionistModel.setEmail(receptionist.getUser().getEmail());
                receptionistModel.setGender(receptionist.getUser().getGender());
                receptionistModel.setPhone(receptionist.getUser().getPhone());
                receptionistModel.setUsername(receptionist.getUser().getUsername());
                receptionistModel.setQualifications(receptionist.getQualifications());
                viewReceptionist.add(receptionistModel);
            }
            return ResponseEntity.of(Optional.of(viewReceptionist));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/viewReceptionist/{email}")
    public ResponseEntity<Optional<ReceptionistModel>> getReceptionist(@PathVariable String email) {
        try {
            Optional<Receptionists> receptionists = receptionRepository.findByUserEmailAndUserActiveTrue(email);
            Receptionists receptionist = receptionists.get();
            ReceptionistModel receptionistModel = new ReceptionistModel();
            receptionistModel.setName(receptionist.getUser().getName());
            receptionistModel.setEmail(receptionist.getUser().getEmail());
            receptionistModel.setGender(receptionist.getUser().getGender());
            receptionistModel.setPhone(receptionist.getUser().getPhone());
            receptionistModel.setUsername(receptionist.getUser().getUsername());
            receptionistModel.setQualifications(receptionist.getQualifications());
            Optional<ReceptionistModel> viewReceptionist = Optional.of(receptionistModel);
            return ResponseEntity.of(Optional.of(viewReceptionist));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PostMapping("/createReceptionist")
    public ResponseEntity<String> createReceptionist(@RequestBody ReceptionistModel receptionistModel) {
        try {

            Users user = new Users();
            user.setName(receptionistModel.getName());
            user.setEmail(receptionistModel.getEmail());
            user.setGender(receptionistModel.getGender());
            user.setUsername(receptionistModel.getUsername());
            user.setPhone(receptionistModel.getPhone());
            user.setPassword(passwordEncoder.encode(receptionistModel.getPassword()));
            user.setRole(RECEPTIONIST);
            user.setActive(true);
            Receptionists receptionist = Receptionists.builder()
                    .user(user)
                    .qualifications(receptionistModel.getQualifications())
                    .build();
            receptionRepository.save(receptionist);
            return ResponseEntity.ok("Successfully created");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PutMapping("/updateReceptionist/{email}")
    public ResponseEntity<String> updateReceptionist(@PathVariable String email,@RequestBody ReceptionistModel receptionistModel) {
        try {
            // Check if the provided email is unique
            Optional<Receptionists> optionalReceptionistWithSameEmail = receptionRepository.findByUserEmailAndUserActiveTrue(email);
            if (optionalReceptionistWithSameEmail.isEmpty()) {
                return ResponseEntity.ok("Doctor with the provided email doesn't exist");
            }

            Receptionists existingReceptionist = optionalReceptionistWithSameEmail.get();

            // If the provided email is different from the existing one, check uniqueness
            if (!email.equals(receptionistModel.getEmail())) {
                Optional<Receptionists> doctorWithEmail = receptionRepository.findByUserEmailAndUserActiveTrue(receptionistModel.getEmail());
                if (doctorWithEmail.isPresent()) {
                    return ResponseEntity.ok("Email already exists for another doctor");
                }
            }
            existingReceptionist.getUser().setName(receptionistModel.getName() == null ? existingReceptionist.getUser().getName() : receptionistModel.getName());
            existingReceptionist.getUser().setEmail(receptionistModel.getEmail() == null ? existingReceptionist.getUser().getEmail() : receptionistModel.getEmail());
            existingReceptionist.getUser().setGender(receptionistModel.getGender() == null ? existingReceptionist.getUser().getGender() : receptionistModel.getGender());
            existingReceptionist.getUser().setPhone(receptionistModel.getPhone() == null ? existingReceptionist.getUser().getPhone() : receptionistModel.getPhone());
            existingReceptionist.getUser().setUsername(receptionistModel.getUsername() == null ? existingReceptionist.getUser().getUsername() : receptionistModel.getUsername());
            existingReceptionist.setQualifications(receptionistModel.getQualifications());


            receptionRepository.save(existingReceptionist);

            return ResponseEntity.ok("Successfully updated");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }

    }
    @DeleteMapping("/deleteReceptionist/{email}")
    public ResponseEntity<String> deleteReceptionist(@PathVariable String email) {
        try{
            Optional<Receptionists> receptionist = receptionRepository.findByUserEmailAndUserActiveTrue(email);
            if(receptionist.isEmpty())
                return ResponseEntity.ok("Receptionist doesn't Exists");
            receptionist.get().getUser().setActive(false);
            receptionRepository.save(receptionist.get());
            return ResponseEntity.ok("Succesfully Deleted");
        }
        catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }
}
