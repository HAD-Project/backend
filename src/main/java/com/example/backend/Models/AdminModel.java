package com.example.backend.Models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminModel {
    private String name;
    private String username;
    private String password;
    private String gender;
}
