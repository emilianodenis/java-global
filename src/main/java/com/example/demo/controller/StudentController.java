package com.example.demo.controller;

import com.example.demo.entity.Student;
import com.example.demo.entity.StudentRepository;
import com.example.demo.model.exception.StudentNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("${apiPrefix}/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping()
    public List<Student> getStudents() {
        var students = studentRepository.findAll();
        students.sort(Comparator.comparing(Student::getLastName, String.CASE_INSENSITIVE_ORDER).thenComparing(Student::getFirstName, String.CASE_INSENSITIVE_ORDER));
        return students;
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Integer id) {
        return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }

    @PutMapping("/{id}")
    public Student replaceStudent(@RequestBody Student newStudent, @PathVariable Integer id) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setFirstName(newStudent.getFirstName());
                    student.setLastName(newStudent.getLastName());
                    student.setEmail(newStudent.getEmail());
                    return studentRepository.save(newStudent);
                })
                .orElseGet(() -> {
                    newStudent.setId(id);
                    return studentRepository.save(newStudent);
                });
    }

    @PostMapping()
    Student newStudent(@RequestBody Student newStudent) {
        return studentRepository.save(newStudent);
    }

    @DeleteMapping("{id}")
    void deleteStudent(@PathVariable Integer id) {
        var student = studentRepository.findById(id);
        if (student.isPresent()) {
            studentRepository.deleteById(id);
        }
    }
}