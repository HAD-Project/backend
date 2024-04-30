package com.example.backend.Entities;

import com.example.backend.cryptography.ConverterUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "referenceNumber")
    private String referenceNumber;

    @Column(name = "display")
    @Convert(converter = ConverterUtil.class)
    private String display;

    @OneToOne(mappedBy = "careContext")
    private Records record;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patients patient;

    private String artefactId;
}
