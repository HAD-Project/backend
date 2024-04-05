package com.example.backend.Controllers;

import com.example.backend.Entities.Receptionists;
import com.example.backend.Entities.Role;
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

}
