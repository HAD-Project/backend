package com.example.backend.Entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "data_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataRequests {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "consent_id")
    private String consentArtefactId;

    @Column(name = "status")
    private String status;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "abha_address")
    private String abhaAddress;

    @Column(name = "expiry")
    private Date expiry;
}
