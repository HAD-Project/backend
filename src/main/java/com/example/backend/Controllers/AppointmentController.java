package com.example.backend.Controllers;

import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Doctors;
import com.example.backend.Entities.Patients;
import com.example.backend.Models.AppointmentModel;
import com.example.backend.Models.DocModel;
import com.example.backend.Repositories.AppointmentRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Repositories.PatientRepository;
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
@RequestMapping("/api/v1/receptionist")
public class AppointmentController {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;
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
                appointmentModel.setDoctorEmail(appointment.getDoctor().getUser().getEmail());
                appointmentModel.setPatientID(appointment.getPatient().getAbhaId());
                appointmentModel.setReceptionistEmail(appointment.getReceptionist().getUser().getEmail());
                appointmentModel.setTime(appointment.getAppointmentTime());
                appointmentModel.setDate(appointment.getAppointmentDate());
                appointmentModel.setRemarks(appointment.getRemarks());
                appointmentModel.setStatus(appointment.getStatus());
                appointmentModel.setType(appointment.getStayType());
                appointmentModel.setDoctorName(appointment.getDoctor().getUser().getName());
                appointmentModel.setAppointmentId(appointment.getAppointmentId());
                appointmentModel.setPatientName(appointment.getPatient().getName());
                viewAppointments.add(appointmentModel);
            }
            return ResponseEntity.of(Optional.of(viewAppointments));
        } catch (Exception e) {
            System.out.println("Error in AppointmentController->getAppointments: " + e.getLocalizedMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/viewDoctorsName")
    public ResponseEntity<List<DocModel>> getDoctors() {
        try {

            List<Doctors> doctors = doctorRepository.findAllByUserActiveTrue();
            List<DocModel> names = new ArrayList<>();
            for(Doctors doctor: doctors){
                DocModel toAdd = new DocModel();
                toAdd.setDoctorName(doctor.getUser().getName());
                toAdd.setDoctorEmail(doctor.getUser().getEmail());
                names.add(toAdd);
            }
            return ResponseEntity.of(Optional.of(names));
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/getPatients")
    public ResponseEntity<List<String>> getPatients(){
        try{
            List<Patients> patients = patientRepository.findAll();
            List<String> names = new ArrayList<>();
            for(Patients patient : patients){
                names.add(patient.getAbhaId());
            }
            return ResponseEntity.of(Optional.of(names));
        }catch(Exception e){
            return null;
        }
    }

    @PutMapping("/updateAppointment/{appointment_id}")
    public ResponseEntity<String> updateDoctor(@PathVariable int appointment_id,@RequestBody AppointmentModel appointmentModel) {
        try {
            Optional<Appointments> appointment = appointmentRepository.findById(appointment_id);
            if(appointment.isEmpty())
                return ResponseEntity.ok("Appointment doesn't Exists");

            Appointments appointmentToBeUpdated = appointment.get();
            appointmentToBeUpdated.getDoctor().getUser().setEmail(appointmentModel.getDoctorEmail());
            appointmentToBeUpdated.getPatient().setAbhaId(appointmentModel.getPatientID());
            appointmentToBeUpdated.setAppointmentDate(appointmentModel.getDate());
            appointmentToBeUpdated.setAppointmentTime(appointmentModel.getTime());
            appointmentToBeUpdated.setStatus(appointmentModel.getStatus());
            appointmentToBeUpdated.setStayType(appointmentModel.getType());
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
