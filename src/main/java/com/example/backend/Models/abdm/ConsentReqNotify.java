package com.example.backend.Models.abdm;

import java.io.Serializable;
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
public class ConsentReqNotify {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class CareContext implements Serializable {
        private String patientReference;
        private String careContextReference;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Patient {
        String id;
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
    public static class ConsentManager {
        private String id;
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
    public static class ConsentDetail {
        private String schemaVersion;
        private String consentId;
        private String createdAt;
        private Patient patient;
        private List<CareContext> careContexts;
        private Purpose purpose;
        private HIP hip;
        private ConsentManager consentManager;
        private List<String> hiTypes;
        private Permission permission;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Notification {
        private String status;
        private String consentId;
        private ConsentDetail consentDetail;
        private String signature;
        private Boolean grantAcknowledgement;
    }

    private Notification notification;
}