package com.example.backend.Models;

import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Doctors;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Data
public class ReceptionistOverviewModel {
    private int noOfPatients;
    private int noOfDoctors;
    private int noOfAppointments;
    private List<Doctors> doctors;
    private List<Appointments> appointments;
}
