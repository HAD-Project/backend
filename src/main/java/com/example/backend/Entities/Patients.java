package com.example.backend.Entities;

import java.util.Date;
import java.util.List;

import com.example.backend.cryptography.ConverterUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Patients")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Patients {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id")
    private int patientId;

    @Column(name = "name")
    @Convert(converter = ConverterUtil.class)
    private String name;

    @Column(name = "abha_id")
    @Convert(converter = ConverterUtil.class)
    private String abhaId;

    @Column(name = "gender")
    @Convert(converter = ConverterUtil.class)
    private String gender;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "mobile_no")
    @Convert(converter = ConverterUtil.class)
    private String mobileNo;

    @JsonIgnore
    @OneToMany(mappedBy = "patient", fetch = FetchType.EAGER)
    List<Records> records;

    @JsonIgnore
    @ManyToMany
    List<Doctors> treatedBy;

    @OneToMany(mappedBy = "patient")
    @JsonIgnore
    List<CareContext> careContexts;

    @Column(name = "link_token", columnDefinition = "varchar(1024)")
    @Convert(converter = ConverterUtil.class)
    private String linkToken;

    @Column(name = "abha_address")
    @Convert(converter = ConverterUtil.class)
    private String abhaAddress;

    @Column(name = "consents")
    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consents> consents;

    @Column(name = "external_records")
    @JsonIgnore
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExternalRecords> externalRecords;
}
