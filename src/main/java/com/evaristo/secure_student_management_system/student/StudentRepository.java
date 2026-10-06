package com.evaristo.secure_student_management_system.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByRegistrationNumber(
            String registrationNumber);

    boolean existsByRegistrationNumber(
            String registrationNumber);

    boolean existsByEmail(String email);
}