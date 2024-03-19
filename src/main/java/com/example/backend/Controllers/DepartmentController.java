package com.example.backend.Controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Entities.Departments;
import com.example.backend.Models.DepartmentModel;
import com.example.backend.Services.DepartmentsServices;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@CrossOrigin
@RestController
@RequestMapping("/api/admin/department")
public class DepartmentController {
    
    @Autowired
    private DepartmentsServices departmentsServices;

    @PostMapping("/createDepartment")
    public ResponseEntity<Departments> postMethodName(@RequestBody DepartmentModel departmentModel) {
        //TODO: process POST request
        try {
            Departments newDepartment = departmentsServices.createDepartments(departmentModel);

            return ResponseEntity.of(Optional.of(newDepartment));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();            
        }
        
    }
    

}
