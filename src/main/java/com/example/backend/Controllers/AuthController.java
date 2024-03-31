package com.example.backend.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.Models.LoginModel;
import com.example.backend.Models.LoginResModel;
import com.example.backend.Models.UserModel;
import com.example.backend.auth.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginModel credentials) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
            String name = authentication.getName();
            String role = authentication.getAuthorities().toString();
            System.out.println("Role: " + role);
            UserModel user = new UserModel();
            user.setUsername(name);
            String token = jwtUtil.createToken(user, role);
            LoginResModel loginRes = new LoginResModel();
            loginRes.setName(name);
            loginRes.setToken(token);
            loginRes.setLoginType(credentials.getLoginType());
            return ResponseEntity.ok(loginRes);
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(403).body("Invalid credentials");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Error occurred while logging in");
        }
    }

}
