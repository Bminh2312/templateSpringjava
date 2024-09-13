package com.example.test.repositories;

import com.example.test.models.StudentImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentImageRepository extends JpaRepository<StudentImage,Long> {
    List<StudentImage> findByStudentId(Long id);
}
