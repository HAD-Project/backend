package com.example.backend.Models.abdm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DataTransferAck {

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class HiRequest {
        private String transactionId;
        private String sessionStatus;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Resp {
        private String requestId;
    }

    private String requestId;
    private String timestamp;
    private HiRequest hiRequest;
    private Resp resp;
}
