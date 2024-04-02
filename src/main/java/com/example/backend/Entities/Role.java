package com.example.backend.Entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.backend.Entities.Permission.*;


@RequiredArgsConstructor
public enum Role {

    DOCTOR(
            Set.of(
                    PATIENT_READ,
                    PATIENT_CREATE,
                    PATIENT_DELETE,
                    PATIENT_UPDATE
            )
    ),
    RECEPTIONIST(
            Set.of(
                    PATIENT_READ,
                    PATIENT_CREATE
            )
    ),
    ADMIN(
            Set.of(
                    ADMIN_READ,
                    ADMIN_CREATE,
                    ADMIN_DELETE,
                    ADMIN_UPDATE,
                    DOCTOR_READ,
                    DOCTOR_CREATE,
                    DOCTOR_DELETE,
                    DOCTOR_UPDATE,
                    RECEPTIONIST_READ,
                    RECEPTIONIST_CREATE,
                    RECEPTIONIST_DELETE,
                    RECEPTIONIST_UPDATE
            )
    );
    @Getter
    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
