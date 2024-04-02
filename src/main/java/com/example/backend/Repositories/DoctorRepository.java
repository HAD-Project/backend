package com.example.backend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.Doctors;



@Repository
public interface DoctorRepository extends JpaRepository<Doctors, Integer> {

    
}
