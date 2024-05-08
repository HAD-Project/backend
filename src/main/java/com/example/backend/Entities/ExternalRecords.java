package com.example.backend.Entities;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "external_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExternalRecords {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String doctorName;

    private Date date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Patients patient;

    private String filePath;

    private String display;

    private Date expiry;

    private String recordType;

    private String consentArtefactId;

    @OneToMany(mappedBy = "externalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RawFiles> files;
}
