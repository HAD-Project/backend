package com.example.backend.Models.abdm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateLinkingTokenReq {
    private String abhaAddress;
    private String name;
    private String gender;
    private int yearOfBirth;   
}
