package com.example.backend.Controllers;

import com.example.backend.Entities.Receptionists;
import com.example.backend.Exception.ResourceNotFound;
import com.example.backend.Repositories.ReceptionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping("/api/admin/receptionist/")
public class AdminReceptionController {

    private final ReceptionRepository receptionRepository;

    public AdminReceptionController(ReceptionRepository receptionRepository) {
        this.receptionRepository = receptionRepository;
    }

    @GetMapping("/viewReceptionist")
    public List<Receptionists> getAllReceptionist(){
        return receptionRepository.findAll();
    }

    @PostMapping("/addReceptionist")
    public Receptionists createReceptionist(@RequestBody Receptionists receptionist){
        receptionist.setUserType(1);
        receptionist.setActive(true);
        return receptionRepository.save(receptionist);
    }
    @PutMapping("/receptionist/{id}")
    public ResponseEntity<Receptionists> updateReceptionistById(@PathVariable int id, @RequestBody Receptionists receptionistsDetails){
        Receptionists receptionists= receptionRepository.findById(id).orElseThrow(()->new ResourceNotFound("Receptionists not exists with id :"+id));

        receptionists.setName(receptionistsDetails.getName());
        receptionists.setEmail(receptionistsDetails.getEmail());
        receptionists.setGender(receptionistsDetails.getGender());
        receptionists.setUsername(receptionistsDetails.getUsername());
        receptionists.setPhone(receptionistsDetails.getPhone());
        receptionists.setQualifications(receptionistsDetails.getQualifications());

        Receptionists updatedreceptionists = receptionRepository.save(receptionists);
        return ResponseEntity.ok(updatedreceptionists);
    }

    @DeleteMapping("/receptionist/{id}")
    public ResponseEntity<Map<String,Boolean>> deleteReceptionist(@PathVariable int id){
        Receptionists receptionist = receptionRepository.findById(id).orElseThrow(()->new ResourceNotFound("Receptionists not exists with id :"+id));
        receptionRepository.delete(receptionist);
        Map<String,Boolean> response = new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return ResponseEntity.ok(response);
    }
}
