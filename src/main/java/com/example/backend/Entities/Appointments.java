package com.example.backend.Entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Appointments")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Appointments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id")
    private int appointmentId;

    
    @ManyToOne
    private Doctors doctor;

    @Column(name = "appt_time")
    private Date appointmentTime;

    @Column(name = "appt_date")
    private Date appointmentDate;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "stay_type")
    private String stayType;

    @Column(name = "status")
    private String status;
}
