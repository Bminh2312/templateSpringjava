package com.example.test.services;

import com.example.test.models.StudentEntity;
import com.example.test.responses.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface IStudentService {
    StudentEntity getStudentById(Long id);
    Page<StudentResponse>getAllStudentsPage(PageRequest pageRequest);
    StudentEntity save(StudentEntity studentEntity);
    StudentEntity update(Long id, StudentEntity studentEntity);
    void delete(Long id);
}
