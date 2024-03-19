package com.example.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DoctorModel {
    private String name;
    private String username;
    private String password;
    private String gender;
    private String qualifications;
    private String department;
}
