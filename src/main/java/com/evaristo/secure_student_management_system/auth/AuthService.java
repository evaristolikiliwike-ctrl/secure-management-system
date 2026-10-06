package com.evaristo.secure_student_management_system.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evaristo.secure_student_management_system.security.JwtService;
import com.evaristo.secure_student_management_system.user.Role;
import com.evaristo.secure_student_management_system.user.User;
import com.evaristo.secure_student_management_system.user.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    // =========================================================
    // REGISTER
    // =========================================================

    public User register(RegisterRequest request) {

        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())

                // Never save password as plain text
                .password(passwordEncoder.encode(request.getPassword()))

                // Public registration creates STUDENT account
                .role(Role.STUDENT)

                .enabled(true)
                .build();

        // Save user into PostgreSQL
        return userRepository.save(user);
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public LoginResponse login(LoginRequest request) {

        // Authenticate username and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Find authenticated user
        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Generate JWT access token
        String accessToken = jwtService.generateToken(user);

        // Generate refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // Return both tokens
        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }

    // =========================================================
    // REFRESH ACCESS TOKEN
    // =========================================================

    public LoginResponse refreshToken(String token) {

        // Find refresh token from database
        RefreshToken refreshToken =
                refreshTokenService.findByToken(token);

        // Check if token is expired or revoked
        refreshTokenService.verifyExpiration(refreshToken);

        // Get user associated with refresh token
        User user = refreshToken.getUser();

        // Generate a new access token
        String accessToken = jwtService.generateToken(user);

        // Return new access token
        return new LoginResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    public void logout(String token) {

        // Revoke refresh token
        refreshTokenService.revokeToken(token);
    }
}