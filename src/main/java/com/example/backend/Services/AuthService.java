package com.example.backend.Services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.Entities.Admins;
import com.example.backend.Entities.Doctors;
import com.example.backend.Repositories.AdminRepository;
import com.example.backend.Repositories.DoctorRepository;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private AdminRepository adminRepository;
    
    @Autowired
    private DoctorRepository doctorRepository;

    // TODO: Receptionist

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Admins admin = adminRepository.findByUsername(userName);
        Doctors doctor = doctorRepository.findByUsername(userName);
        System.out.println("Admin: " + admin);
        System.out.println("Doctor: " + doctor);
        // Receptionist
        if(admin == null && doctor == null) {
            throw new UsernameNotFoundException(userName);
        }
        else if(admin != null && doctor != null) {
            System.out.println("Duplicate usernames found: " + userName);
            return null;
        }
        else if(admin != null) {
            List<String> roles = new ArrayList<>();
            roles.add("ADMIN");
            UserDetails userDetails = 
                org.springframework.security.core.userdetails.User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
            return userDetails;
        }
        else if(doctor != null) {
            List<String> roles = new ArrayList<>();
            roles.add("DOCTOR");
            UserDetails userDetails = 
                org.springframework.security.core.userdetails.User.builder()
                .username(doctor.getUsername())
                .password(doctor.getPassword())
                .roles("DOCTOR")
                .build();
            return userDetails;
        }
        else {
            System.out.println("Error occurred in AuthService.loadUserByUsername");
            return null;
        }
    }

}
