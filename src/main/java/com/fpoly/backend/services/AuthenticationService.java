package com.fpoly.backend.services;

import com.fpoly.backend.dto.*;
import com.fpoly.backend.entities.Admin;
import com.fpoly.backend.entities.Instructor;
import com.fpoly.backend.entities.Student;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {
    public String generateToken(String email, String role);

    public String generateRoleStudent(Student student);

    public String generateRoleInstructor(Instructor instructor);

    public String generateRoleAdmin(Admin admin);

    AuthenticationResponse authenticationAndGenerateToken (AuthenticationRequest request);

    AuthenticationResponse createAuthResponse(String token);

    SignedJWT verifyToken(String token) throws ParseException, JOSEException;

    public void logout(Logout request) throws ParseException, JOSEException;

    public IntrospectResponse introspect(IntrospectRequest request);

    public AuthenticationResponse refreshToken(Refresh request) throws ParseException, JOSEException;
}
