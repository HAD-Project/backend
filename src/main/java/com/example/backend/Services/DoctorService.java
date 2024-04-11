package com.example.backend.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.Config.JwtService;
import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.CareContext;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Repositories.CareContextRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
import com.example.backend.Entities.Records;
import com.example.backend.Models.RecordModel;
import com.example.backend.Models.abdm.CareContextPatient;
import com.example.backend.Models.abdm.LinkCareContextReq;
import com.example.backend.Repositories.RecordRepository;

import reactor.core.publisher.Mono;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileAlreadyExistsException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Service
@Transactional
public class DoctorService {

   @Autowired
   private DoctorRepository doctorRepository;

   @Autowired
   private PatientRepository patientRepository;

//    @Autowired
//    private Appointments appointmentService;

   @Autowired
   private RecordRepository recordRepository;

   @Autowired
   private JwtService jwtService;

   @Autowired
   private CareContextRepository careContextRepository;

   @Autowired
   private ABDMServices abdmServices;

//    public List<Appointments> getAppointments(Doctors doctor) {
//        return appointmentService.findAppointmentsByDoctor(doctor);
//    }

   private final String recordBasePath = "/home/shrutik/HAD/records/";

   public List<Patients> getPatients(String token) {
       String email = jwtService.extractUsername(token);
       System.out.println("Email: " + email);
       Doctors doctor = doctorRepository.findByUserEmailAndUserActiveTrue(email).get();
       List<Patients> patients = doctor.getTreats();
       return patients;
   }

   public Patients getPatient(int patientId) {
       Patients patient = patientRepository.findByPatientId(patientId);
       return patient;
   }

   public Records createRecord(String token, RecordModel toAdd) throws Exception {
       String email = jwtService.extractUsername(token);
       Records newRecord = new Records();

       Doctors doctor = doctorRepository.findByUserEmailAndUserActiveTrue(email).get();
       Patients patient = patientRepository.findByPatientId(toAdd.getPatientId());
       newRecord.setDoctor(doctor);
       newRecord.setPatient(patient);
       newRecord.setRecordType(toAdd.getRecordType());
       newRecord.setStatus("available");
       newRecord.setDisplay(toAdd.getDisplay());
       DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSS'Z'");
       Date date = df.parse(toAdd.getDate());
       newRecord.setDate(date);

       CareContext careContext = new CareContext();
       careContext.setPatient(patient);
       careContext.setDisplay(toAdd.getDisplay());

       try {
           File record = new File(recordBasePath + toAdd.getPatientId() + "_" + toAdd.getDate());
           if(record.createNewFile()) {
               FileWriter writer = new FileWriter(record);
               writer.write(toAdd.getText());
               writer.close();
               newRecord.setFilePath(recordBasePath + toAdd.getPatientId() + "_" + toAdd.getDate());
               Records savedRecord = recordRepository.save(newRecord);
               careContext.setRecord(newRecord);
               CareContext savedCareContext = careContextRepository.save(careContext);
               newRecord.setCareContext(careContext);
               savedRecord = recordRepository.save(newRecord);
               if(patient.getAbhaAddress() != null) {
                    abdmServices.linkRecord(savedRecord, patient, careContext)
                        .subscribe((args) -> {System.out.println("Record linked");});
               }
               return savedRecord;
           }
           else {
               System.out.println("File already exists");
               return null;
           }
       }
       catch (IOException e) {
           System.out.println("Error creating record");
           return null;
       }
   }


   public List<RecordModel> getRecords(int patientId) throws FileNotFoundException {
    Patients patient = patientRepository.findByPatientId(patientId);
    List<Records> records = patient.getRecords();
    List<RecordModel> res = new ArrayList<>();
    for(Records r : records) {
        File f = new File(r.getFilePath());
        Scanner sc = new Scanner(f);
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) {
            sb.append(sc.nextLine());
        }
        sc.close();
        RecordModel toAdd = new RecordModel();
        toAdd.setText(sb.toString());
        toAdd.setRecordType(r.getRecordType());
        res.add(toAdd);
    }
    return res;
   }
}
