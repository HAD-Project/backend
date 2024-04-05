package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.example.backend.Repositories.AdminRepository;
import com.example.backend.Repositories.DoctorRepository;
import com.example.backend.Services.ABDMServices_Shrutik;


@SpringBootApplication
@PropertySource("classpath:application-dev.properties")
public class BackendApplication {

	@Autowired
	AdminRepository adminRepository;

	@Autowired
	DoctorRepository doctorRepository;

	@Autowired
	ABDMServices_Shrutik abdmServices;

	public static void main(String[] args) {
		System.out.println("Working");
		SpringApplication.run(BackendApplication.class, args);
	}
}
