package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Entities.ExternalRecords;
import java.util.List;


public interface ExternalRecordsRepository extends JpaRepository<ExternalRecords, String> {
    List<ExternalRecords> findByConsentArtefactId(String consentArtefactId);
    
    void deleteAll(Iterable<? extends ExternalRecords> entities);
    
}
