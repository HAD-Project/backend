package com.example.backend.Entities;

import com.example.backend.cryptography.ConverterUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @OneToOne(targetEntity = Users.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private Users user;

    @Column(name = "qualifications")
    @Convert(converter = ConverterUtil.class)
    private String qualifications;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department")
    @JsonIgnore
    private Departments department;

    @ManyToMany(mappedBy = "treatedBy")
    List<Patients> treats;

    @Column(name = "consents")
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consents> consents;
}
