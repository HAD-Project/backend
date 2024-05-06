package com.example.backend.Services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.example.backend.Config.JwtService;
import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.CareContext;
import com.example.backend.Entities.Consents;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.ExternalRecords;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.RawFiles;
import com.example.backend.Repositories.CareContextRepository;
import com.example.backend.Repositories.ConsentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
import com.example.backend.Repositories.RawFilesRepository;
import com.example.backend.Entities.Records;
import com.example.backend.Models.FileModel;
import com.example.backend.Models.FileUpload;
import com.example.backend.Models.PatientDetailsModel;
import com.example.backend.Models.RecordModel;

import com.example.backend.Models.frontend.RequestRecords;
import com.example.backend.Repositories.RecordRepository;
import com.example.backend.cryptography.CryptographyUtil;

import jakarta.servlet.http.HttpServletResponse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.MediaType;

import javassist.bytecode.ExceptionTable;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.text.DateFormat;
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

   @Autowired
   private FHIRServices fhirServices;

   @Autowired
   private ConsentRepository consentRepository;

   @Autowired
   private CryptographyUtil cryptographyUtil;

   @Autowired
   private RawFilesRepository rawFilesRepository;
   
   @Value("${raw_files_base_path}")
   private String rawFilesBasePath;

//    public List<Appointments> getAppointments(Doctors doctor) {
//        return appointmentService.findAppointmentsByDoctor(doctor);
//    }

    @Value("${record_base_path}")
    private String recordBasePath;

    public List<Patients> getPatients(String token) {
       String email = jwtService.extractUsername(token);
       System.out.println("Email: " + email);
       Doctors doctor = doctorRepository.findByUserEmailAndUserActiveTrue(email).get();
       List<Patients> patients = doctor.getTreats();
       return patients;
   }

    public PatientDetailsModel getPatient(int patientId) {
        Patients patient = patientRepository.findByPatientId(patientId);

        PatientDetailsModel toReturn = new PatientDetailsModel();
        toReturn.setPatientId(patientId);
        toReturn.setAbhaId(patient.getAbhaId());
        toReturn.setDob(patient.getDob());
        toReturn.setGender(patient.getGender());
        toReturn.setMobileNo(patient.getMobileNo());
        toReturn.setName(patient.getName());
        return toReturn;
    }

   public Records createRecord(String token, RecordModel toAdd, String txnId) throws Exception {
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
               if(toAdd.getRecordType().equals("Prescription")) {
                writer.write(cryptographyUtil.encrypt(fhirServices.createPrescription(doctor, patient, toAdd.getPrescriptionList())));
               }
               else if(toAdd.getRecordType().equals("HealthDocumentRecord")) {
                writer.write(cryptographyUtil.encrypt(toAdd.getText()));
               }
               else {
                writer.write(cryptographyUtil.encrypt(toAdd.getText()));
               }
               writer.close();
               newRecord.setFilePath(recordBasePath + toAdd.getPatientId() + "_" + toAdd.getDate());
               Records savedRecord = recordRepository.save(newRecord);
               careContext.setRecord(newRecord);
               CareContext savedCareContext = careContextRepository.save(careContext);
               newRecord.setCareContext(careContext);
               savedRecord = recordRepository.save(newRecord);

               if(toAdd.getRecordType().equals("HealthDocumentRecord")) {
                List<RawFiles> files = rawFilesRepository.findByTxnId(txnId);
                for(RawFiles rawFile: files) {
                    rawFile.setRecord(savedRecord);
                    rawFilesRepository.save(rawFile);
                }
               }
            //    if(patient.getAbhaAddress() != null) {
            //         abdmServices.linkRecord(savedRecord, patient, careContext)
            //             .subscribe((args) -> {System.out.println("Record linked");});
            //    }
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
        if(r.getRecordType().equals("Prescription")) {
            String json = sb.toString();
            json = cryptographyUtil.decrypt(json);
            toAdd.setPrescriptionList(new ArrayList<>());
            toAdd.getPrescriptionList().addAll(fhirServices.toPrescriptionModel(json));
        }
        else if(r.getRecordType().equals("HealthDocumentRecord")) {
            List<FileModel> files = new ArrayList<>();
            for(RawFiles rawFile: r.getFiles()) {
                FileModel toAddFile = new FileModel();
                toAddFile.setId(rawFile.getId());
                toAddFile.setFileName(rawFile.getName());
                files.add(toAddFile);
            }
            toAdd.setFiles(files);
            String text = sb.toString();
            text = cryptographyUtil.decrypt(text);
            toAdd.setText(text);
        }
        else {
            String text = sb.toString();
            text = cryptographyUtil.decrypt(text);
            toAdd.setText(text);
        }
        toAdd.setRecordType(r.getRecordType());
        toAdd.setDate(r.getDate().toString());
        toAdd.setDisplay(r.getDisplay());
        res.add(toAdd);
    }

    List<ExternalRecords> externalRecords = patient.getExternalRecords();
    for(ExternalRecords r: externalRecords) {
        File f = new File(r.getFilePath());
        Scanner sc = new Scanner(f);
        StringBuilder sb = new StringBuilder();
        while(sc.hasNextLine()) {
            sb.append(sc.nextLine());
        }
        sc.close();
        RecordModel toAdd = new RecordModel();
        if(r.getRecordType().equals("Prescription")) {
            String json = sb.toString();
            json = cryptographyUtil.decrypt(json);
            toAdd.setPrescriptionList(new ArrayList<>());
            toAdd.getPrescriptionList().addAll(fhirServices.toPrescriptionModel(json));
        }
        else {
            String text = sb.toString();
            text = cryptographyUtil.decrypt(text);
            toAdd.setText(sb.toString());
        }
        toAdd.setRecordType(r.getRecordType());
        toAdd.setDate(r.getDate().toString());
        toAdd.setDisplay(r.getDisplay());
        res.add(toAdd);
    }
    
    return res;
   }

    public void requestRecords(String token, RequestRecords req) {
        String doctorEmail = jwtService.extractUsername(token);
        Doctors doctor = doctorRepository.findByUserEmailAndUserActiveTrue(doctorEmail).get();
        Patients patient = patientRepository.findByAbhaId(req.getAbhaId());

        Consents toAdd = new Consents();
        toAdd.setTimeStamp(new Date());
        toAdd.setText(req.getExplanation());
        toAdd.setCode(req.getMetaCode());
        toAdd.setPatient(patient);
        toAdd.setDoctor(doctor);
        toAdd.setHiTypes(req.getRecordType());
        toAdd.setAccessMode("VIEW");
        toAdd.setStatus("REQUESTED");

        toAdd.setDateFrom(req.getFromDate());
        toAdd.setDateTo(req.getToDate());
        toAdd.setDataEraseAt(req.getExpiryDate());
        toAdd = consentRepository.save(toAdd);
        
        abdmServices.requestConsent(toAdd);
    }

    public void uploadFile(String txnId, FileUpload req) {

        for(MultipartFile file: req.getFiles()) {
            RawFiles toAdd = new RawFiles();
            toAdd.setTxnId(txnId);
            toAdd.setType(file.getContentType());
            toAdd.setName(file.getOriginalFilename());
            
            String filePath = rawFilesBasePath + UUID.randomUUID() +  "_" + file.getOriginalFilename();
            toAdd.setPath(filePath);
            File toWrite = new File(filePath);
            try {
                if(toWrite.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(toWrite);
                    fos.write(cryptographyUtil.encrypt(file.getBytes()));
                    fos.close();
                    rawFilesRepository.save(toAdd);
                }
            }
            catch (Exception e) {
                System.out.println("Error in DoctorService->uploadFile: " + e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    public ResponseEntity<InputStreamResource> downloadFile(String fileId, HttpServletResponse res) {
        RawFiles file = rawFilesRepository.findById(fileId).get();

        try {
            File f = new File(file.getPath());
            InputStream inputStream = new FileInputStream(f);

            byte encrypted[] = inputStream.readAllBytes();
            inputStream.close();
            byte decrypted[] = cryptographyUtil.decrypt(encrypted);
            InputStream decryptedInputStream = new ByteArrayInputStream(decrypted);

            HttpHeaders headers = new HttpHeaders();
            if(file.getType().equals("application/pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            }
            else if(file.getType().equals("image/png")) {
                headers.setContentType(MediaType.IMAGE_PNG);
            }
            else {
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
            headers.setContentDispositionFormData("filename", file.getName());
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(headers.getContentType())
                    .body(new InputStreamResource(decryptedInputStream));
        }
        catch (Exception e) {
            System.out.println("Error in DoctorService->downloadFile: " + e.getLocalizedMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
} 
