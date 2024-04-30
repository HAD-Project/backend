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
public class DataTransferRes {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entries {
        private String content;
        private String media;
        private String checksum;
        private String careContextReference;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DhPublicKey {
        private String expiry;
        private String parameters;
        private String keyValue;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyMaterial {
        private String cryptoAlg;
        private String curve;
        private DhPublicKey dhPublicKey;
        private String nonce;
    }
    
    private int pageNumber;
    private int pageCount;
    private String transactionId;
    private List<Entries> entries;
    private KeyMaterial keyMaterial;
}
