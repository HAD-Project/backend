package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.backend.Entities.Admins;
import com.example.backend.Entities.Doctors;
import com.example.backend.Repositories.AdminRepository;
import com.example.backend.Repositories.DoctorRepository;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BackendApplication {

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	DoctorRepository doctorRepository;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void initialiseDatabase() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Admins toAdd = new Admins();
		toAdd.setName("Admin");
		toAdd.setUsername("admin");
		toAdd.setGender("X");
		toAdd.setUserType(0);
		toAdd.setPassword(encoder.encode("123"));
		if(!adminRepository.existsById(1)) {
			adminRepository.save(toAdd);
		}

		Doctors doctor = new Doctors();
		doctor.setName("Doctor");
		doctor.setUsername("doctor");
		doctor.setGender("Y");
		doctor.setUserType(1);
		doctor.setPassword(encoder.encode("123"));
		if(!doctorRepository.existsById(1)) {
			doctorRepository.save(doctor);
		}
	}

}
