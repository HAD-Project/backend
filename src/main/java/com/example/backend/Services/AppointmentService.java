package com.example.backend.Services;

import java.util.List;
import java.util.Optional;

import com.example.backend.Config.JwtService;
import com.example.backend.Entities.Patients;
import com.example.backend.Entities.Receptionists;
import com.example.backend.Models.AppointmentModel;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
import com.example.backend.Repositories.ReceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Repositories.AppointmentRepository;


@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ReceptionRepository receptionistRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

//    public List<Appointments> findAppointmentsByDoctor(Doctors doctor) {
//        return appointmentRepository.findByDoctor(doctor);
//    }
//
    public Appointments createAppointment(String token, AppointmentModel toAdd){
        String email = jwtService.extractUsername(token);
        Appointments appointment = new Appointments();
        Receptionists receptionist = receptionistRepository.findByUserEmailAndUserActiveTrue(email).get();
        Patients patient = patientRepository.findByAbhaId(toAdd.getPatientID());
        Optional<Doctors> tempdoctor = doctorRepository.findByUserEmailAndUserActiveTrue(toAdd.getDoctorEmail());
        Doctors doctor;
        if(tempdoctor == null){
            System.out.println("tempdoctor is null");
            doctor = doctorRepository.findByUserEmail("doctor@mail.com");
        }
        else
            doctor=doctorRepository.findByUserEmail(toAdd.getDoctorEmail());
        System.out.println(toAdd);
        appointment.setReceptionist(receptionist);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(toAdd.getTime());
        appointment.setAppointmentDate(toAdd.getDate());
        appointment.setStatus(toAdd.getStatus());
        appointment.setRemarks(toAdd.getRemarks());
        appointment.setStayType(toAdd.getType());

        return appointmentRepository.save(appointment);
    }
}
