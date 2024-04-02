package com.example.backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.Entities.Admins;


@Repository
public interface AdminRepository extends JpaRepository<Admins, Integer> {


} 
