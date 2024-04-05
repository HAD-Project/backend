package com.example.backend.Repositories;

import com.example.backend.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer>{
    Optional<Users> findByEmail(String email);


}
