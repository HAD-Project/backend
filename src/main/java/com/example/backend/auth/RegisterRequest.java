package com.example.backend.auth;

import com.example.backend.Entities.Departments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.backend.Entities.Role;

import java.math.BigInteger;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String name;
    private String username;
    private String email;
    private String password;
    private String gender;
    private BigInteger phone;
    private String qualification;
    private int department_id;
    private Role role;
}
