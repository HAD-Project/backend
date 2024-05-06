package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.Entities.RawFiles;
import java.util.List;



public interface RawFilesRepository extends JpaRepository<RawFiles, String> {
    public List<RawFiles> findByTxnId(String txnId);
}
