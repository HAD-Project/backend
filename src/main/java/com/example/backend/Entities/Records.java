package com.example.backend.Entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "Records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Records {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "file_path")
    private String filePath;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setDoctor(Doctors doctor) {
        this.doctor = doctor;
    }

    public void setReceptionist(Receptionists receptionist) {
        this.receptionist = receptionist;
    }

    public void setPatient(Patients patient) {
        this.patient = patient;
    }

    public String getFilePath() {
        return filePath;
    }

    public Doctors getDoctor() {
        return doctor;
    }

    public Receptionists getReceptionist() {
        return receptionist;
    }

    public Patients getPatient() {
        return patient;
    }

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctors doctor;

    @ManyToOne
    @JoinColumn(name = "receptionist_id")
    private Receptionists receptionist;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patients patient;

    @Column(name = "record_type")
    private String recordType;

    @Column(name = "date")
    private Date date;

    @Column(name = "status")
    private String status;

    @OneToOne
    @JoinColumn(name = "context_id", referencedColumnName = "referenceNumber")
    CareContext careContext;

    @Column(name = "display")
    private String display;
}
