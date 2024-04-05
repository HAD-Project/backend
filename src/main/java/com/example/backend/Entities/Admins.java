package com.example.backend.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Admins")
@Getter
@Setter
@AllArgsConstructor
public class Admins extends Users {
    
}
