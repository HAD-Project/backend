package com.example.backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Records {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Doctors doctor;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Receptionists receptionist;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patients patient;
}
