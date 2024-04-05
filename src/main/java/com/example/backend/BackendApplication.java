package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.example.backend.Repositories.AdminRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Services.ABDMServices_Shrutik;


import com.example.backend.Services.ABDMServices;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@PropertySource("classpath:application-dev.properties")
public class BackendApplication {

	@Autowired
	ABDMServices abdmServices;
	
	@Autowired
	AdminRepository adminRepository;

	@Autowired
	DoctorRepository doctorRepository;

	// @Autowired
	// ABDMServices_Shrutik abdmServices2;

	public static void main(String[] args) {
		System.out.println("Working");
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void receiveKey() {
		try {
			abdmServices.getPublicKey();
			abdmServices.initiateSession();
			System.out.println(abdmServices.getRsaKey());
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
