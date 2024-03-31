package com.example.backend.Services;

import java.util.List;
import java.util.ArrayList;

import com.example.backend.Entities.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.backend.Repositories.UserRepository;

@Service
public class UserService {

    // @Autowired
    // private UserRepository userRepository;

    // @Override
    // public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    //     Users user = userRepository.findByUsername(userName);
    //     List<String> roles = new ArrayList<>();
    //     roles.add("USER");
    //     UserDetails userDetails = 
    //         org.springframework.security.core.userdetails.User.builder()
    //         .username(user.getUsername())
    //         .password(user.getPassword())
    //         .roles(roles.toArray(new String[0]))
    //         .build();
        
    //     return userDetails;
    // }

}
