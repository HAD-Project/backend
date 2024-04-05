//package com.example.backend.Services;
//
//import java.util.List;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.example.backend.Entities.Appointments;
//import com.example.backend.Entities.Doctors;
//import com.example.backend.Repositories.AppointmentRepository;
//
//
//@Service
//public class AppointmentService {
//
//    @Autowired
//    private AppointmentRepository appointmentRepository;
//
//    public List<Appointments> findAppointmentsByDoctor(Doctors doctor) {
//        return appointmentRepository.findByDoctor(doctor);
//    }
//}
