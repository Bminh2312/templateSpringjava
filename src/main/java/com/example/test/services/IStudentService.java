package com.example.test.services;

import com.example.test.models.Rank;
import com.example.test.models.StudentEntity;
import com.example.test.responses.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IStudentService {

    List<StudentEntity> getAllStudent();
    StudentEntity getStudentById(Long id);
    Page<StudentResponse>getAllStudentsPage(PageRequest pageRequest);
    StudentEntity save(StudentEntity studentEntity);
    StudentEntity update(Long id, StudentEntity studentEntity);
    Page<StudentResponse>searchStudents(String name, PageRequest pageRequest);
    Page<StudentResponse>filter(Rank rank, int dob,PageRequest pageRequest);
    void delete(Long id);
}
