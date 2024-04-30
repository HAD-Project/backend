package com.example.backend.Models.abdm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConsentReqOnInit {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConsentRequest {
        private String id;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String requestId;
    }

    ConsentRequest consentRequest;
    Response response;
}
