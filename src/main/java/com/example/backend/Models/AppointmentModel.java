package com.example.backend.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class AppointmentModel {
    int doctorId;
    int receptionistId;
    int patientId;
    private Date time;
    private Date date;
    private String remarks;
    private String stayType;
    private String status;
}
