package com.example.backend.Models.abdm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PatientCareContextDiscoverRes {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Patient {
        private String referenceNumber;
        private String display;
        private List<CareContextModel> careContexts;
        private List<String> matchedBy;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Response {
        String requestId;
    }

    private String requestId;
    private String timestamp;
    private String transactionId;
    private Patient patient;
    private Response resp;
}
