package com.example.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceptionistPatientModel {
    private String abha_available;
    private String name;
    private String sex;
    private Integer age;
    private String dob;
    private String phoneNumber;
    private String address;
}
