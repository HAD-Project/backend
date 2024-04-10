package com.example.backend.Models.abdm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LinkCareContextReq {
    private String abhaAddress;
    List<CareContextPatient> patient;
    @Override
    public String toString() {
        return "LinkCareContextReq [abhaAddress=" + abhaAddress + ", patient=" + patient + "]";
    }
    
}