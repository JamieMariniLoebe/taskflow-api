package com.taskflow.auth.service;

import com.taskflow.auth.dto.AuthResponse;
import com.taskflow.auth.dto.LoginRequest;
import com.taskflow.auth.dto.RegisterRequest;
import com.taskflow.common.util.JwtUtil;
import com.taskflow.user.persistence.UserEntity;
import com.taskflow.user.persistence.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest credentials) {

        UserEntity userEntity = new UserEntity();

        // Hash password
        String hashPw = passwordEncoder.encode(credentials.getPassword());

        userEntity.setUsername(credentials.getUsername());
        userEntity.setPassword(hashPw);
        userEntity.setEmail(credentials.getEmail());
        userEntity.setRoles("User");

        // Save user
        userRepository.save(userEntity);

        // Generate token
        String token = jwtUtil.generateToken(userEntity.getUsername());

        // Return AuthResponse
        return new AuthResponse(token);

    }

    public AuthResponse login(LoginRequest credentials) {
        // Convert credentials to something AuthenticationManager can understand
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

        // Verify credentials and authenticate
        authenticationManager.authenticate(authToken);

        String token = jwtUtil.generateToken(credentials.getUsername());

        // Return AuthResponse
        return new AuthResponse(token);

    }

}
