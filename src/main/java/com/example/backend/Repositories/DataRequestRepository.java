package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.DataRequests;

@Repository
public interface DataRequestRepository extends JpaRepository<DataRequests, String> {
    
    DataRequests findByTransactionId(String transactionid);

    DataRequests findByRequestId(String requestId);
    
}
