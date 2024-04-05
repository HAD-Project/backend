package com.example.backend.Controllers;

import com.example.backend.Entities.Departments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Receptionists;
import com.example.backend.Models.DoctorModel;
import com.example.backend.Models.ReceptionistModel;
import com.example.backend.Repositories.ReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/reception")
public class ReceptionController {
    @Autowired
    private ReceptionRepository receptionRepository;
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
            receptionistModel.setQualifications(receptionist.getQualifications());
            Optional<ReceptionistModel> viewReceptionist = Optional.of(receptionistModel);
            return ResponseEntity.of(Optional.of(viewReceptionist));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @PutMapping("/updateReceptionist/{email}")
    public ResponseEntity<String> updateReceptionist(@PathVariable String email,@RequestBody ReceptionistModel receptionistModel) {
        try {
            Optional<Receptionists> receptionist = receptionRepository.findByUserEmailAndUserActiveTrue(email);
            if(receptionist.isEmpty())
                return ResponseEntity.ok("Receptionist doesn't Exists");
            Receptionists receptionistToBeUpdated = receptionist.get();
            receptionistToBeUpdated.getUser().setName(receptionistModel.getName());
            receptionistToBeUpdated.getUser().setEmail(receptionistModel.getEmail());
            receptionistToBeUpdated.getUser().setGender(receptionistModel.getGender());
            receptionistToBeUpdated.setQualifications(receptionistModel.getQualifications());
            receptionRepository.save(receptionistToBeUpdated);
            return ResponseEntity.ok("Succesfully Updated");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    @DeleteMapping("/deleteReceptionist/{email}")
    public ResponseEntity<String> deleteReceptionist(@PathVariable String email) {
        try{
            Optional<Receptionists> receptionist = receptionRepository.findByUserEmailAndUserActiveTrue(email);
            if(receptionist.isEmpty())
                return ResponseEntity.ok("Doctor doesn't Exists");
            receptionist.get().getUser().setActive(false);
            receptionRepository.save(receptionist.get());
            return ResponseEntity.ok("Succesfully Deleted");
        }
        catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }
}
