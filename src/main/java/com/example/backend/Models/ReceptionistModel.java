package com.example.backend.Models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class ReceptionistModel {
    private String name;
    private String email;
    //    private String password;
    private String gender;
    private String qualifications;
}
