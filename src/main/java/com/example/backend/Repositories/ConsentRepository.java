package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Entities.Consents;
import java.util.Optional;



public interface ConsentRepository extends JpaRepository<Consents, String> {
    Optional<Consents> findById(String id);

    Consents findByConsentReqId(String consentReqId);

    Consents findByConsentArtefactId(String consentArtefactId);
}
