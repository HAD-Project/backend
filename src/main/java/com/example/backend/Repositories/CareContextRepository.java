package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Entities.CareContext;

public interface CareContextRepository extends JpaRepository<CareContext, String> {
    
}
