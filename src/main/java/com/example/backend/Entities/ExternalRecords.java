package com.example.backend.Entities;

import java.util.Date;
import java.util.List;

import com.example.backend.cryptography.ConverterUtil;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Convert;
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

    @Convert(converter = ConverterUtil.class)
    private String doctorName;

    private Date date;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Patients patient;

    @Convert(converter = ConverterUtil.class)
    private String filePath;

    @Convert(converter = ConverterUtil.class)
    private String display;

    private Date expiry;

    @Convert(converter = ConverterUtil.class)
    private String recordType;

    private String consentArtefactId;

    @OneToMany(mappedBy = "externalRecord", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<RawFiles> files;
}
