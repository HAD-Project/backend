package com.example.backend.Services;

import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Models.PatientInfoModel;
import com.example.backend.Models.ReceptionistOverviewModel;
import com.example.backend.Repositories.AppointmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceptionistOverviewService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    public ReceptionistOverviewModel getRecepOverviewData(){
        ReceptionistOverviewModel res=new ReceptionistOverviewModel();

        List<Doctors> doctors = doctorRepository.findAll();
        List<Appointments> appointments = appointmentRepository.findAll();
        int noOfPatients=patientRepository.findAll().size();

        res.setAppointments(appointments);
        res.setDoctors(doctors);
        res.setNoOfPatients(noOfPatients);
        res.setNoOfDoctors(doctors.size());
        res.setNoOfAppointments(appointments.size());

        return res;
    }
}
