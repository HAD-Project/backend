package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.DataTransfers;

@Repository
public interface DataTransferRepository extends JpaRepository<DataTransfers, String> {
    public DataTransfers findByReqId(String reqId);
}
