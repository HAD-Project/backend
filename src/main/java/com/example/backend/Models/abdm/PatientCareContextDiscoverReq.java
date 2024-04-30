package com.example.backend.Models.abdm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientCareContextDiscoverReq {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Patient {
        private String id;
        List<Identifiers> verifiedIdentifiers;
        List<Identifiers> unverifiedIdentifiers;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Identifiers {
        private String type;
        private String value;
    }

    @Getter
    @Setter
    @NoArgsConstructor  
    @AllArgsConstructor
    public static class Response {
        private String requestId;
    }

    private String transactionId;
    private String name;
    private String gender;
    private int yearOfBirth;
    Patient patient;
}