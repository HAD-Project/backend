package com.example.backend.Models.abdm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GenerateLinkingTokenRes {
    private Long abhaNumber;
    private String linkToken;
    private String abhaAddress;

    @Override
    public String toString() {
        return "{abhaNumber: " + abhaNumber + ", abhaAddress: " + abhaAddress + "}";
    }
}