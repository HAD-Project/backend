package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.Entities.Appointments;
import com.example.backend.Entities.Doctors;
import java.util.List;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, Integer> {
    
    List<Appointments> findByDoctor(Doctors doctor);

}
