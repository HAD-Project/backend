package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    
    @GetMapping("/overview")
    public String getOverview() {
        return new String();
    }

    @GetMapping("/appointments")
    public String getAppointments(@RequestParam Integer doctorId) {
        return new String();
    }
    
    @PostMapping("/request")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    
}
