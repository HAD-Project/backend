package com.example.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccessTokenModel {

    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private int refreshExpiresIn;
}
