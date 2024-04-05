package com.example.backend.auth;

import com.example.backend.Entities.*;
import com.example.backend.Repositories.*;
import lombok.RequiredArgsConstructor;
import com.example.backend.Token.Token;
import com.example.backend.Token.TokenRepository;
import com.example.backend.Token.TokenType;
import com.example.backend.Config.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService ;
    private final UserRepository repository;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final DepartmentRepository departmentRepository;
    private final ReceptionRepository receptionRepository;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = Users.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .phone(request.getPhone())
                .gender(request.getGender())
                .build();
        var savedUser = repository.save(user);
        if (request.getRole() == Role.ADMIN) {
                var admin = Admins.builder()
                        .user(savedUser)
                        .build();
                adminRepository.save(admin);

        } else if (request.getRole()==Role.DOCTOR) {
            var department = departmentRepository.findById(request.getDepartment_id());
            var doctor = Doctors.builder()
                    .user(savedUser)
                    .qualifications(request.getQualification())
                    .department(department)
                    .build();
            doctorRepository.save(doctor);
        } else if (request.getRole()==Role.RECEPTIONIST) {
            var receptionist = Receptionists.builder()
                    .user(savedUser)
                    .qualifications(request.getQualification())
                    .build();
            receptionRepository.save(receptionist);
        }
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(Users savedUser, String jwtToken) {
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user,jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void revokeAllUserTokens(Users user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
