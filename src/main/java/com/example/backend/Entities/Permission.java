package com.example.backend.Entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),
    DOCTOR_READ("doctor:read"),
    DOCTOR_UPDATE("doctor:update"),
    DOCTOR_CREATE("doctor:create"),
    DOCTOR_DELETE("doctor:delete"),

    PATIENT_READ("patient:read"),
    PATIENT_UPDATE("patient:update"),
    PATIENT_CREATE("patient:create"),
    PATIENT_DELETE("patient:delete"),

    RECEPTIONIST_READ("receptionist:read"),
    RECEPTIONIST_UPDATE("receptionist:update"),
    RECEPTIONIST_CREATE("receptionist:create"),
    RECEPTIONIST_DELETE("receptionist:delete")

    ;
    @Getter
    private final String permission;
}
