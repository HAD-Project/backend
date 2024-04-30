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
public class Keys {
    private String privateKey;
    private String publicKey;
    private String x509PublicKey;
    private String nonce;
}
