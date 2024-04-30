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
public class DataTransferOnReq {
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HiRequest {
        private String transactionId;
        private String sessionStatus;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String requestId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Error {
        private String code;
        private String message;
    }

    private HiRequest hiRequest;
    private Error error;
    private Response response;

}
