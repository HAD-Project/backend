package com.example.backend.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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
    private List<PrescriptionModel> prescriptionList;
}
