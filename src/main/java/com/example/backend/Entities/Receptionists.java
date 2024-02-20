package com.example.backend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Receptionists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receptionists extends Users{

    @Column(name = "qualifications")
    private String qualifications;
}
