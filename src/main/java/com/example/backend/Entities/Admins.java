package com.example.backend.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Admins")
@Getter
@Setter
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class Admins {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_id")
    private int adminId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

}
