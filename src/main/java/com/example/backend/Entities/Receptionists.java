package com.example.backend.Entities;

import java.util.List;

import com.example.backend.cryptography.ConverterUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Receptionists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Receptionists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receptionist_id")
    private int receptionistId;

    @OneToOne(targetEntity = Users.class,cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id",referencedColumnName = "user_id")
    private Users user;

    @Column(name = "qualifications")
    @Convert(converter = ConverterUtil.class)
    private String qualifications;

    @JsonIgnore
    @OneToMany
    private List<Appointments> appointments;


}
