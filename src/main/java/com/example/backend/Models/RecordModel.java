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
    int recordId;
    int doctorId;
    int patientId;
    String text;
    String recordType;
    String date;
    String display;
}
