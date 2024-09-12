package com.example.test.services;

import com.example.test.models.Rank;
import com.example.test.models.StudentEntity;
import com.example.test.repositories.StudentRepository;
import com.example.test.responses.StudentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService implements IStudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<StudentEntity> getAllStudent() {
        return studentRepository.findAll();
    }

    @Override
    public StudentEntity getStudentById(Long id) {
        Optional<StudentEntity> student = studentRepository.findById(id);
        return student.orElse(null);
    }

    @Override
    public Page<StudentResponse> getAllStudentsPage(PageRequest pageRequest) {
        return studentRepository.findAll(pageRequest).map(StudentResponse::fromStudent);
    }

    @Override
    public StudentEntity save(StudentEntity studentEntity) {
        return studentRepository.save(studentEntity);
    }

    @Override
    public StudentEntity update(Long id, StudentEntity studentEntity) {
        StudentEntity student = this.getStudentById(id);
        if(student!= null){
            student = StudentEntity.builder()
                    .id(id)
                    .name(studentEntity.getName())
                    .city(studentEntity.getCity())
                    .dob(studentEntity.getDob())
                    .rank(studentEntity.getRank())
                    .build();
            return studentRepository.save(student);
        }
        return null;
    }

    @Override
    public Page<StudentResponse> searchStudents(String name, PageRequest pageRequest) {
        return studentRepository.findByNameContainingIgnoreCaseOrderByCreateAtAsc(name,pageRequest).map(StudentResponse::fromStudent);

    }

    @Override
    public Page<StudentResponse> filter(Rank rank, int dob, PageRequest pageRequest) {
        return studentRepository.filterRankAndDOB(rank, dob, pageRequest).map(StudentResponse::fromStudent);
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
