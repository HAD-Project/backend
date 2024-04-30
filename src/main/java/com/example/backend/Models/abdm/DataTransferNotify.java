package com.example.backend.Models.abdm;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataTransferNotify {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Notifier {
        private String type;
        private String id;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Notification {

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        public static class StatusNotification {

            @Getter
            @Setter
            @AllArgsConstructor
            @NoArgsConstructor
            public static class StatusResponses {
                private String careContextReference;
                private String hiStatus;
                private String description;
            }

            private String sessionStatus;
            private String hipId;
            private List<StatusResponses> statusResponses;

        }

        private String consentId;
        private String transactionId;
        private Notifier notifier;
        private StatusNotification statusNotification;
    }
    
    private String requestId;
    private String timestamp;
    private Notification notification;
}
