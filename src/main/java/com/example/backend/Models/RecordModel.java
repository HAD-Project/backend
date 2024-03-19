package com.example.backend.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RecordModel {
    int doctorId;
    int patientId;
    String text;
    int recordType;
    String date;
}
