package com.example.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceptionistPatientModel {
    private String abha_available;
    private String abha;
    private String name;
    private String sex;
    private Integer age;
    private String dob;
    private String phoneNumber;
    private String address;
    private String abhaAddress;
    
    @Override
    public String toString() {
        return "ReceptionistPatientModel [abha_available=" + abha_available + ", abha=" + abha + ", name=" + name
                + ", sex=" + sex + ", age=" + age + ", dob=" + dob + ", phoneNumber=" + phoneNumber + ", address="
                + address + ", abhaAddress=" + abhaAddress + "]";
    }
    
    
}
