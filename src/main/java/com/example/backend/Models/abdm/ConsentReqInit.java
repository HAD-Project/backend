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
public class ConsentReqInit {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Purpose {
        private String text;
        private String code;
        private String refUri = "http://example.com";
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
    public static class HIU {
        private String id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Requester {
        private String name;
        private Identifier identifier;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Identifier {
        private String type;
        private String value;
        private String system = "https://www.mciindia.org";
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
        private String unit = "HOUR";
        private int value = 0;
        private int repeats = 0;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Permission {
        private String accessMode;
        DateRange dateRange;
        private String dataEraseAt;
        private Frequency frequency;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Consent {
        private Purpose purpose;
        private Patient patient;
        private HIU hiu;
        private Requester requester;
        private List<String> hiTypes;
        private Permission permission;
    }
    
    private String requestId;
    private String timestamp;
    private Consent consent;
}
