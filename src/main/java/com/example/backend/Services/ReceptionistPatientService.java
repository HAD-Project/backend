package com.example.backend.Services;

import com.example.backend.Entities.Patients;
import com.example.backend.Models.ReceptionistPatientModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceptionistPatientService {

    public Patients registerPatient(ReceptionistPatientModel receptionistPatientModel){
        Patients res=new Patients();
        res.setName("ankjhj");
        return res;
    }

    public List<Patients> getPatients(){
        List<Patients> res =new ArrayList<>();

        return res;
    }
}
