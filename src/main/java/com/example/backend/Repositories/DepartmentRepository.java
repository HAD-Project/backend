package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.Departments;


@Repository
public interface DepartmentRepository extends JpaRepository<Departments, Integer> {

    Departments findById(int id);
    Departments findByName(String name);
    
}