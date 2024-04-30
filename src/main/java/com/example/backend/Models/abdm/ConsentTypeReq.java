package com.example.backend.Models.abdm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ConsentTypeReq {
    private String requestId;
    private String timestamp;
    
    // This is consent artefact ID
    private String consentId;
}
