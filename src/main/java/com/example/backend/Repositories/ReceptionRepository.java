package com.example.backend.Repositories;

import com.example.backend.Entities.Doctors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.Receptionists;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceptionRepository extends JpaRepository<Receptionists, Integer> {
    List<Receptionists> findAllByUserActiveTrue();
    Optional<Receptionists> findByUserEmailAndUserActiveTrue(String email);
}
