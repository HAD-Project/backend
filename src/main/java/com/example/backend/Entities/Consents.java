package com.example.backend.Entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Consents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Consents {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "consent_req_id")
    private String consentReqId;

    @Column(name = "consent_artefact_id")
    private String consentArtefactId;

    @Column(name = "timestamp")
    private Date timeStamp;

    @Column(name = "text")
    private String text;

    @Column(name = "code")
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    private Patients patient;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctors doctor;

    @Column(name = "hi_types")
    private List<String> hiTypes;

    @Column(name = "access_mode")
    private String accessMode;

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @Column(name = "data_erase_at")
    private Date dataEraseAt;

    @Column(name = "status")
    private String status;
}
