package com.example.backend.Models.abdm.auth.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientAuthOnInitReq {
    private String authMethod;
    private String healthid;
}