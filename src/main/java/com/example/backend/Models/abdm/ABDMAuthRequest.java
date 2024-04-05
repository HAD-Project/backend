package com.example.backend.Models.abdm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ABDMAuthRequest {
    private String clientId;
    private String clientSecret;

}
