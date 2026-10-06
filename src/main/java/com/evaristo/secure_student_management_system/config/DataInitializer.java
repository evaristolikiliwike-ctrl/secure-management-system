package com.evaristo.secure_student_management_system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.evaristo.secure_student_management_system.user.Role;
import com.evaristo.secure_student_management_system.user.User;
import com.evaristo.secure_student_management_system.user.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // =========================
            // ADMIN USER
            // =========================

            if (!userRepository.existsByUsername("admin")) {

                User admin = User.builder()
                        .username("admin")
                        .email("admin@studentmanagement.com")
                        .password(
                                passwordEncoder.encode("Admin@123")
                        )
                        .role(Role.ADMIN)
                        .enabled(true)
                        .build();

                userRepository.save(admin);
            }

            // =========================
            // TEACHER USER
            // =========================

            if (!userRepository.existsByUsername("teacher")) {

                User teacher = User.builder()
                        .username("teacher")
                        .email("teacher@studentmanagement.com")
                        .password(
                                passwordEncoder.encode("Teacher@123")
                        )
                        .role(Role.TEACHER)
                        .enabled(true)
                        .build();

                userRepository.save(teacher);
            }

            // =========================
            // STUDENT USER
            // =========================

            if (!userRepository.existsByUsername("student")) {

                User student = User.builder()
                        .username("student")
                        .email("student@studentmanagement.com")
                        .password(
                                passwordEncoder.encode("Student@123")
                        )
                        .role(Role.STUDENT)
                        .enabled(true)
                        .build();

                userRepository.save(student);
            }
        };
    }
}