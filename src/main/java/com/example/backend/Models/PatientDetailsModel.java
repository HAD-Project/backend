package com.example.backend.Models;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetailsModel {
    private int patientId; 
    private String name;
    private String abhaId;
    private String gender;
    private Date dob;
    private String mobileNo;
    List<RecordModel> records;  
}
