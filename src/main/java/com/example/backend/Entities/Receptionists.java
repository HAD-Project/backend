package com.example.backend.Entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Receptionists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receptionists extends Users {

    @Column(name = "qualifications")
    private String qualifications;

    @JsonIgnore
    @OneToMany
    private List<Appointments> appointments;

    @Column(name="active",nullable = false)
    private  boolean active;
}
