package com.example.backend.Models.abdm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ABDMAuthResponse {
    private String accessToken;
    private long expiresIn;
    private long refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
}
