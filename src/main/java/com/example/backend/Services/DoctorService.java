package com.example.backend.Services;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
import com.example.backend.Entities.Records;
import com.example.backend.Models.RecordModel;
import com.example.backend.Repositories.RecordRepository;
import java.io.File;
import java.io.FileWriter;


@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private RecordRepository recordRepository;

    public List<Appointments> getAppointments(Doctors doctor) {
        return appointmentService.findAppointmentsByDoctor(doctor);
    }

    private final String recordBasePath = "/home/shrutik/HAD/records/";

    public List<Patients> getPatients(int doctorId) {
        Doctors doctor = doctorRepository.findByUserId(doctorId);
        List<Patients> patients = doctor.getTreats();
        return patients;
    }

    public Patients getPatient(int patientId) {
        Patients patient = patientRepository.findById(patientId);
        return patient;
    }

    public Records createRecord(RecordModel toAdd) {
        Records newRecord = new Records();
        Doctors doctor = doctorRepository.findByUserId(toAdd.getDoctorId());
        Patients patient = patientRepository.findById(toAdd.getPatientId());
        String filePath = recordBasePath + toAdd.getPatientId() + "_" + toAdd.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date date = sdf.parse(toAdd.getDate());
            newRecord.setDoctor(doctor);
            newRecord.setPatient(patient);
            newRecord.setFilePath(filePath);
            newRecord.setRecordType(toAdd.getRecordType());
            newRecord.setDate(date);
            File record = new File(filePath);
            if(record.createNewFile()) {
                FileWriter writer = new FileWriter(record);
                writer.write(toAdd.getText());
                writer.close();
                System.out.println("Record created");
                return recordRepository.save(newRecord);
            }
            else {
                System.out.println("File already exists");
                return null;
            }
        }
        catch (Exception e) {
            System.out.println("Error creating record");
            e.printStackTrace();
            return null;
        }
    }

    public List<RecordModel> getRecords(int patientId) {
        System.out.println("Patient id: " + patientId);
        Patients patient = patientRepository.findById(patientId);
        System.out.println("Fetched patient id: " + patient.getPatientId());
        List<Records> records = patient.getRecords();
        List<RecordModel> res = new ArrayList<>();
        try {
            for(Records r: records) {
                String filePath = r.getFilePath();
                File f = new File(filePath);
                Scanner sc = new Scanner(f);
                StringBuilder sb = new StringBuilder();
                while(sc.hasNextLine()) {
                    sb.append(sc.nextLine());
                }
                String text = sb.toString();
                RecordModel toAdd = new RecordModel();
                toAdd.setRecordId(r.getId());
                toAdd.setDoctorId(r.getDoctor().getUserId());
                toAdd.setPatientId(r.getPatient().getPatientId());
                toAdd.setText(text);
                toAdd.setDate(r.getDate().toString());
                toAdd.setRecordType(r.getRecordType());
                res.add(toAdd);
                sc.close();
            }
            return res;
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
