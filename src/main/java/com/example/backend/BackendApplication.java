package com.example.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.backend.Services.ABDMServices;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BackendApplication {

	@Autowired
	private ABDMServices abdmServices;

	public static void main(String[] args) {
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
