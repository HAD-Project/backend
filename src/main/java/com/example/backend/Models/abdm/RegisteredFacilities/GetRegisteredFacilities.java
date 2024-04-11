package com.example.backend.Models.abdm.RegisteredFacilities;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetRegisteredFacilities {
    private Bridge bridge;
    private List<Service> services;
}
