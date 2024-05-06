package com.example.backend.Models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RecordModel {
    int recordId;
    int doctorId;
    int patientId;
    String text;
    String recordType;
    String date;
    String display;
    private List<PrescriptionModel> prescriptionList;
    private List<FileModel> files;
}
