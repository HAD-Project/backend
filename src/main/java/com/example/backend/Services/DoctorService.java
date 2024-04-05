package com.example.backend.Services;

import java.util.List;

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
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;


@Service
public class DoctorService {

   @Autowired
   private DoctorRepository doctorRepository;

   @Autowired
   private PatientRepository patientRepository;

//    @Autowired
//    private Appointments appointmentService;

   @Autowired
   private RecordRepository recordRepository;

//    public List<Appointments> getAppointments(Doctors doctor) {
//        return appointmentService.findAppointmentsByDoctor(doctor);
//    }

   private final String recordBasePath = "/home/shrutik/HAD/records/";

   public List<Patients> getPatients(int doctorId) {
       Doctors doctor = doctorRepository.findByDoctorId(doctorId).get();
       List<Patients> patients = doctor.getTreats();
       return patients;
   }

   public Patients getPatient(int patientId) {
       Patients patient = patientRepository.findByPatientId(patientId);
       return patient;
   }

   public Records createRecord(RecordModel toAdd) {
       Records newRecord = new Records();
       Doctors doctor = doctorRepository.findByDoctorId(toAdd.getDoctorId()).get();
       Patients patient = patientRepository.findByPatientId(toAdd.getPatientId());
       newRecord.setDoctor(doctor);
       newRecord.setPatient(patient);
       try {
           File record = new File(recordBasePath + toAdd.getPatientId() + "_" + toAdd.getDate());
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
       catch (IOException e) {
           System.out.println("Error creating record");
           e.printStackTrace();
           return null;
       }

   }

}
