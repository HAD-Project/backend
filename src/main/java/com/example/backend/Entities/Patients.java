package com.example.backend.Entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import jakarta.persistence.ManyToMany;
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
    private String name;

    @Column(name = "abha_id")
    private String abhaId;

    @Column(name = "gender")
    private String gender;

    @Column(name = "dob")
    private Date dob;

    @Column(name = "mobile_no")
    private String mobileNo;

    @JsonIgnore
    @OneToMany(mappedBy = "patient")
    List<Records> records;

    @JsonIgnore
    @ManyToMany
    List<Doctors> treatedBy;
}
