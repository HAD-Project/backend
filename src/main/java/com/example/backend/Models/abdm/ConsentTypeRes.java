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
public class ConsentTypeRes {
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class HIP {
        private String id;
        private String name;
        private String type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class HIU {
        private String id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Patient {
        private String id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Purpose {
        private String text;
        private String code;
        private String refUri;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Identifer {
        private String value;
        private String type;
        private String system;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Requester {
        private String name;
        private Identifer identifer;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class DateRange {
        private String from;
        private String to;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Frequency {
        private String unit;
        private String value;
        private String repeats;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Permission {
        private String accessMode;
        private DateRange dateRange;
        private String dataEraseAt;
        private Frequency frequency;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class CareContext {
        private String patientReference;
        private String careContextReference;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ConsentManager {
        private String id;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class ConsentDetail {
        private String consentId;
        private HIP hip;
        private HIU hiu;
        private List<String> hiTypes;
        private Patient patient;
        private Purpose purpose;
        private String createdAt;
        private Requester requester;
        private Permission permission;
        private String lastUpdated;
        private List<CareContext> careContexts;
        private String schemaVersion;
        private ConsentManager consentManager;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Consent {
        private String status;
        private ConsentDetail consentDetail;
        private String signature;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Error {
        private String code;
        private String message;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Response {
        private String requestId;
    }

    private Consent consent;
    private Error error;
    private Response response;
}
