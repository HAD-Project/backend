package com.example.backend.Repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.Entities.Records;
import com.example.backend.Entities.Patients;;

@Repository
public interface RecordRepository extends JpaRepository<Records, Integer> {
    List<Records> findByPatient(Patients patient);
}
