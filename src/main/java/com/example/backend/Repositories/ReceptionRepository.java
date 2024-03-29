package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.Receptionists;
@Repository
public interface ReceptionRepository extends JpaRepository<Receptionists, Integer> {

    Receptionists findByname(String name);
}
