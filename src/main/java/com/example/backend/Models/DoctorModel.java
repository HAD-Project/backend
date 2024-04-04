package com.example.backend.Models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class DoctorModel {
    private String name;
    private String username;
    private String password;
    private String gender;
    private String qualifications;
    private String department;
}
