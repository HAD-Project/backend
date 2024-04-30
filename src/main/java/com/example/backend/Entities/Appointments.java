package com.example.backend.Entities;

import java.util.Date;

import com.example.backend.cryptography.ConverterUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @JoinColumn(name = "doctor_id")
    private Doctors doctor;

    @ManyToOne
    @JoinColumn(name = "receptionist_id")
    private Receptionists receptionist;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patients patient;

    @Column(name = "appt_time")
    private Date appointmentTime;

    @Column(name = "appt_date")
    private Date appointmentDate;

    @Column(name = "remarks")
    @Convert(converter = ConverterUtil.class)
    private String remarks;

    @Column(name = "stay_type")
    @Convert(converter = ConverterUtil.class)
    private String stayType;

    @Column(name = "status")
    @Convert(converter = ConverterUtil.class)
    private String status;
}
