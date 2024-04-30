package com.example.backend.Entities;

import java.util.ArrayList;
import java.util.List;

import com.example.backend.cryptography.ConverterUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Departments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Departments {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dept_id")
    private int id;

    @Column(name = "name",unique = true,nullable = false)
    @Convert(converter = ConverterUtil.class)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", orphanRemoval = true)
    @JsonIgnoreProperties("department")
    private List<Doctors> doctors = new ArrayList<>(); 
}
