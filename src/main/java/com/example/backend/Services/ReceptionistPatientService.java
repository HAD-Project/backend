package com.example.backend.Services;

import com.example.backend.Entities.Patients;
import com.example.backend.Models.PatientInfoModel;
import com.example.backend.Models.PatientUpdateModel;
import com.example.backend.Models.ReceptionistPatientModel;
import com.example.backend.Repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReceptionistPatientService {

    @Autowired
    PatientRepository patientRepository;

    public Patients registerPatient(ReceptionistPatientModel receptionistPatientModel) throws ParseException {
        Patients newPatient=new Patients();

        newPatient.setName(receptionistPatientModel.getName());
        newPatient.setGender(receptionistPatientModel.getSex());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        Date date = dateFormat.parse(receptionistPatientModel.getDob());
        newPatient.setDob(date);
        newPatient.setMobileNo(receptionistPatientModel.getPhoneNumber());
        newPatient.setAbhaId(receptionistPatientModel.getAbha());
//                add address ,age,blood group

        return patientRepository.save(newPatient);
    }

    public List<Patients> getPatients(){
        List<Patients> res = new ArrayList<>();
        res = patientRepository.findAll();

        return res;
    }

    public PatientInfoModel getPatientData(int patientId){
        PatientInfoModel res=new PatientInfoModel();

        Patients patient = patientRepository.findByPatientId(patientId);

        res.setPatientId(patient.getPatientId());
        res.setName(patient.getName());
        res.setAbhaId(patient.getAbhaId());
        res.setGender(patient.getGender());
        res.setMobileNo(patient.getMobileNo());
        res.setDob(patient.getDob());

        return res;
    }

    public String updatePatient(PatientUpdateModel patientUpdateModel){

        Patients patientUpdated = patientRepository.findByPatientId(patientUpdateModel.getPatientId());
        patientUpdated.setName(patientUpdateModel.getName());
        patientUpdated.setGender(patientUpdateModel.getGender());
        patientUpdated.setDob(patientUpdateModel.getDob());
        patientUpdated.setMobileNo(patientUpdateModel.getMobileNo());
        String res;
        try{
            patientRepository.save(patientUpdated);
            res="Patient Updated Successfully";
        }catch (Exception e){
            res=e.toString();
        }
        return res;
    }

    public String deletePatient(int patientId){
        Patients patientDeleted = patientRepository.findByPatientId(patientId);
        patientDeleted.setName(null);
        patientDeleted.setGender(null);
        patientDeleted.setDob(null);
        patientDeleted.setMobileNo(null);

        String res;
        try {
            patientRepository.save(patientDeleted);
            res="Deleted Successfully";
        } catch (Exception e) {
            res=e.toString();
        }
        return res;
    }

}
