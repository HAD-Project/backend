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
public class ConsentAck {
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class Acknowledgement {
        private String status;
        private String consentId;
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
    public static class Resp {
        private String requestId;
    }
    
    private String requestId;
    private String timestamp;
    private Acknowledgement acknowledgement;
    private Error error;
    private Resp resp;
}
