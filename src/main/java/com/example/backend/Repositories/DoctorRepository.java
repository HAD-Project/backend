package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.Doctors;


@Repository
public interface DoctorRepository extends JpaRepository<Doctors, Integer> {

    Doctors findByUserId(int userId);
    Doctors findByName(String name);
    Doctors findByUsernameAndPassword(String username, String password);
    
}
