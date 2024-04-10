package com.example.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientProfileModel {
    private String healthIdNumber;
    private String healthId;
    private String mobile;
    private String firstName;
    private String middleName;
    private String lastName;
    private String name;
    private String yearOfBirth;
    private String dayOfBirth;
    private String monthOfBirth;
    private String gender;
    private String email;
    private String profilePhoto;
    private String stateCode;
    private String districtCode;
    private String pincode;
    private String address;
    private String stateName;
    private String districtName;
    private String subdistrictName;
    private String villageName;
    private String townName;
    private String wardName;           
}
