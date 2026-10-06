package com.evaristo.secure_student_management_system.student;

import java.util.List;

import org.springframework.stereotype.Service;

import com.evaristo.secure_student_management_system.exception.ResourceNotFoundException;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get student by ID
    public Student getStudentById(Long id) {

        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Student not found with id: " + id
                        )
                );
    }

    // Create student
    public Student createStudent(Student student) {

        if (studentRepository.existsByRegistrationNumber(
                student.getRegistrationNumber())) {

            throw new RuntimeException(
                    "Registration number already exists"
            );
        }

        if (studentRepository.existsByEmail(
                student.getEmail())) {

            throw new RuntimeException(
                    "Email already exists"
            );
        }

        return studentRepository.save(student);
    }

    // Update student
    public Student updateStudent(
            Long id,
            Student updatedStudent) {

        Student existingStudent = getStudentById(id);

        existingStudent.setFirstName(
                updatedStudent.getFirstName()
        );

        existingStudent.setLastName(
                updatedStudent.getLastName()
        );

        existingStudent.setRegistrationNumber(
                updatedStudent.getRegistrationNumber()
        );

        existingStudent.setEmail(
                updatedStudent.getEmail()
        );

        existingStudent.setPhone(
                updatedStudent.getPhone()
        );

        existingStudent.setGender(
                updatedStudent.getGender()
        );

        existingStudent.setDateOfBirth(
                updatedStudent.getDateOfBirth()
        );

        existingStudent.setUser(
                updatedStudent.getUser()
        );

        return studentRepository.save(existingStudent);
    }

    // Delete student
    public void deleteStudent(Long id) {

        Student student = getStudentById(id);

        studentRepository.delete(student);
    }
}