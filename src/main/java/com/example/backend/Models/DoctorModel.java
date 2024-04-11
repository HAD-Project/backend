package com.example.backend.Models;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class DoctorModel {
    private String name;
    private String email;
    private String username;
    private String password;
    private String gender;
    private BigInteger phone;
    private String qualifications;
    private String department;
}
