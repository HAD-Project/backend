package com.example.backend.Models;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AppointmentModel {
    String doctorEmail;
    String receptionistEmail;
    String patientID;
    String patientName;
    private Date time;
    private Date date;
    private String remarks;
    private String type;
    private String status;
    private String doctorName;
    private int appointmentId;
}
