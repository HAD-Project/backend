package com.example.backend.Controllers;

import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Records;
import com.example.backend.Models.RecordModel;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

}
