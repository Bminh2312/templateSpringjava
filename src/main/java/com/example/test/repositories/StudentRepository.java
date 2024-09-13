package com.example.test.repositories;

import com.example.test.models.Rank;
import com.example.test.models.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,Long> {
    Page<StudentEntity> findByNameContainingIgnoreCaseOrderByCreateAtAsc(String name, PageRequest pageRequest);

    @Query("SELECT s FROM StudentEntity s where s.city like LOWER(CONCAT('%',:city,'%'))")
    Page<StudentEntity> findByCity(String city,PageRequest pageRequest);

    @Query("SELECT s From StudentEntity s Where s.rank = :rank AND Year(s.dob) = :dob")
    Page<StudentEntity> filterRankAndDOB(Rank rank, int dob, PageRequest pageRequest);
//
//    @Query("SELECT s FROM StudentEntity s" +
//            "WHERE ")
}
