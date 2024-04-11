package com.example.backend.Models.abdm.RegisteredFacilities;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    private String id;
    private String name;
    private List<String> types;
    private Map<String, String> endpoints;
    private boolean active;    
}
