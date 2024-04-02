package com.example.backend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Doctors  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id")
    private int doctorId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "qualifications")
    private String qualifications;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    @JsonIgnoreProperties("doctors")
    private Departments department;

    @ManyToMany(mappedBy = "treatedBy")
    List<Patients> treats;
}
