package com.example.backend.Entities;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.backend.Models.abdm.ConsentReqNotify.CareContext;

@Entity
@Table(name = "data_transfers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataTransfers {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "req_id")
    private String reqId;

    @Column(name = "data_push_url")
    private String dataPushUrl;

    @Column(name = "public_key")
    private String key;

    @Column(name = "nonce")
    private String nonce;

    @Column(name = "from_date")
    private String fromDate;

    @Column(name = "to_date")
    private String toDate;

    @Column(name = "transaction_id")
    private String transactionId;

    private String abhaAddress;

    @ElementCollection(targetClass = CareContext.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "external_care_contexts", joinColumns = @JoinColumn(name = "care_context_artefact"))
    @Column(name = "care_contexts", nullable = false)
    private List<CareContext> careContexts;
}
