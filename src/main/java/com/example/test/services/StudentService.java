package com.example.test.services;

import ch.qos.logback.core.util.StringUtil;
import com.example.test.dtos.StudentImageDTO;
import com.example.test.models.Rank;
import com.example.test.models.StudentEntity;
import com.example.test.models.StudentImage;
import com.example.test.repositories.StudentImageRepository;
import com.example.test.repositories.StudentRepository;
import com.example.test.responses.StudentResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StudentService implements IStudentService{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentImageRepository studentImageRepository;

    @Override
    public StudentImage saveStudentImage(Long studentId, StudentImageDTO studentImageDTO) {
        StudentEntity student = getStudentById(studentId);
        int size =studentImageRepository.findByStudentId(studentId).size();
        if(size>= 4){
            throw new InvalidParameterException("Mỗi sinh viên tối đa 4 ảnh");
        }
        StudentImage studentImage = StudentImage.builder()
                .student(student)
                .imageUrl(studentImageDTO.getImageUrl())
                .build();

        return studentImageRepository.save(studentImage);
    }

    @Override
    public List<StudentImage> getAllStudentImages(Long studentId) {
        return studentImageRepository.findByStudentId(studentId);
    }

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
