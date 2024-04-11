package com.example.backend.Controllers;

import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Departments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Records;
import com.example.backend.Models.AppointmentModel;
import com.example.backend.Models.DoctorModel;
import com.example.backend.Repositories.AppointmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
import com.example.backend.Repositories.ReceptionRepository;
import com.example.backend.Services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/createAppointment")
    public ResponseEntity<Appointments> createAppointment(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody AppointmentModel toAdd){
        try {
            Appointments appointment = appointmentService.createAppointment(token.split(" ")[1], toAdd);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/viewAppointments")
    public ResponseEntity<List<AppointmentModel>> getAppointments() {
        try {
            List<Appointments> appointments = appointmentRepository.findAll();
            List<AppointmentModel> viewAppointments = new ArrayList<>();
            for(Appointments appointment: appointments){
                AppointmentModel appointmentModel = new AppointmentModel();
                appointmentModel.setDoctorId(appointment.getDoctor().getDoctorId());
                appointmentModel.setPatientId(appointment.getPatient().getPatientId());
                appointmentModel.setReceptionistId(appointment.getReceptionist().getReceptionistId());
                appointmentModel.setTime(appointment.getAppointmentTime());
                appointmentModel.setDate(appointment.getAppointmentDate());
                appointmentModel.setRemarks(appointment.getRemarks());
                appointmentModel.setStatus(appointment.getStatus());
                appointmentModel.setStayType(appointment.getStayType());
                viewAppointments.add(appointmentModel);
            }
            return ResponseEntity.of(Optional.of(viewAppointments));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/updateAppointment/{appointment_id}")
    public ResponseEntity<String> updateDoctor(@PathVariable int appointment_id,@RequestBody AppointmentModel appointmentModel) {
        try {
            Optional<Appointments> appointment = appointmentRepository.findById(appointment_id);
            if(appointment.isEmpty())
                return ResponseEntity.ok("Appointment doesn't Exists");

            Appointments appointmentToBeUpdated = appointment.get();
            appointmentToBeUpdated.getDoctor().setDoctorId(appointmentModel.getDoctorId());
            appointmentToBeUpdated.getReceptionist().setReceptionistId(appointmentModel.getReceptionistId());
            appointmentToBeUpdated.getPatient().setPatientId(appointmentModel.getPatientId());
            appointmentToBeUpdated.setAppointmentDate(appointmentModel.getDate());
            appointmentToBeUpdated.setAppointmentTime(appointmentModel.getTime());
            appointmentToBeUpdated.setStatus(appointmentModel.getStatus());
            appointmentToBeUpdated.setStayType(appointmentModel.getStayType());
            appointmentToBeUpdated.setRemarks(appointmentModel.getRemarks());
            appointmentRepository.save(appointmentToBeUpdated);
            return ResponseEntity.ok("Successfully Updated");
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @DeleteMapping("/deleteAppointment/{appointment_id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable int appointment_id) {
        try{
            Optional<Appointments> appointment = appointmentRepository.findById(appointment_id);
            if(appointment.isEmpty())
                return ResponseEntity.ok("Appointment doesn't Exists");
            appointmentRepository.deleteById(appointment_id);
            return ResponseEntity.ok("Succesfully Deleted");
        }
        catch (Exception e){
            return ResponseEntity.status(500).build();
        }
    }
}
