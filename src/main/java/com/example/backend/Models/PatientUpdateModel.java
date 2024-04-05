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
public class PatientUpdateModel {
    private int patientId;
    private String name;
    private String abhaId;
    private String gender;
    private Date dob;
    private String mobileNo;
}
