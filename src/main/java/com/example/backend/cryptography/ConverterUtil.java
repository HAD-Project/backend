package com.example.backend.cryptography;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;

@Component
public class ConverterUtil implements AttributeConverter<String, String> {
    @Autowired
    CryptographyUtil cryptographyUtil;


    @Override
    public String convertToDatabaseColumn(String s) {
        return cryptographyUtil.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return cryptographyUtil.decrypt(s);
    }
}
