package com.studyai.auth;

import com.studyai.auth.dto.AuthResponse;
import com.studyai.auth.dto.RegisterRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.studyai.auth.dto.LoginRequest;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;  

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
         // step 1: TODO - check if email already exists, throw an error if so
        if (userRepository.existsByEmail(request.getEmail())) {
    throw new IllegalArgumentException("Email already registered");
}
       // step 2: create a new User, set name/email, and set the HASHED password
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // step 3: TODO - save the user using userRepository
        userRepository.save(user);
        // step 4: return null for now - we'll fix this once JWT exists
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail());
    } 
    public AuthResponse login(LoginRequest request) {
        // step 1: TODO - find the user by email, throw an error if not found
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

            
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }   
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getName(), user.getEmail());
}
} 