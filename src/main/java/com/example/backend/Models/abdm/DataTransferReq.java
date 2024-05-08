package com.example.backend.Models.abdm;

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
public class DataTransferReq {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Consent {
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
    public static class DhPublicKey {
        private String expiry;
        private String parameters;
        private String keyValue;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class KeyMaterial {
        private String cryptoAlg;
        private String curve;
        private DhPublicKey dhPublicKey;
        private String nonce;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class HiRequest {
        private Consent consent;
        private DateRange dateRange;
        private String dataPushUrl;
        private KeyMaterial keyMaterial;
    }

    private String requestId;
    private String timestamp;
    private String transactionId;
    private HiRequest hiRequest;
}
