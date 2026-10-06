package com.evaristo.secure_student_management_system.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.evaristo.secure_student_management_system.user.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}