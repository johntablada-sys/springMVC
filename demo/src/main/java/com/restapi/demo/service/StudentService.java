package com.restapi.demo.service;

import com.restapi.demo.dto.StudentDto;

import java.util.List;

public interface StudentService {

    StudentDto createStudent(StudentDto studentDto);

    StudentDto getStudentById(Long id);

    List<StudentDto> getAllStudents();

    StudentDto updateStudent(Long id, StudentDto studentDto);

    StudentDto patchStudent(Long id, StudentDto studentDto);

    void deleteStudentById(Long id);
}
