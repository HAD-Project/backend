package com.example.backend.Repositories;

import com.example.backend.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer>{
    Optional<Users> findByEmail(String email);


}
