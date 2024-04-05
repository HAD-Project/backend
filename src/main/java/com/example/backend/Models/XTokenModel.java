package com.example.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class XTokenModel {
    private String token;
    private int expiresIn;
    private String refreshToken;
    private int refreshExpiresIn;
}
