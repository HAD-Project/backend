package com.example.backend.Models.abdm.RegisteredFacilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Bridge {
    private String id;
    private String name;
    private String url;
    private boolean active;
    private boolean blocklisted;
}
