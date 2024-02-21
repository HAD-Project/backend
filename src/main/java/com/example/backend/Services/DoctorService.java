package com.example.backend.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Repositories.AppointmentRepository;
import com.example.backend.Repositories.DoctorRepository;

@Service
public class DoctorService {
    
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentService appointmentService;

    public List<Appointments> getAppointments(Doctors doctor) {
        return appointmentService.findAppointmentsByDoctor(doctor);
    }

}
