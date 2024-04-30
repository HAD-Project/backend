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
public class HIUConsentReqNotify {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsentArtefact {
        private String id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Notification {
        private String consentRequestId;
        private String status;
        List<ConsentArtefact> consentArtefacts;
    }
    
    private String requestId;
    private String timeStamp;
    private Notification notification;
}