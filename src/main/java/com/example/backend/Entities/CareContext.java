package com.example.backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "care_contexts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CareContext {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referenceNumber")
    private int referenceNumber;

    @Column(name = "display")
    private String display;

    @OneToOne(mappedBy = "careContext")
    private Records record;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patients patient;

}
