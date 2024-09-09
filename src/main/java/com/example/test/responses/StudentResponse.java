package com.example.test.responses;

import com.example.test.models.Rank;
import com.example.test.models.StudentEntity;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class StudentResponse extends BaseResponse{
    private Long id;
    private String name;
    private String city;
    private LocalDate DOB;
    private Rank rank;
    public static StudentResponse fromStudent(StudentEntity studentEntity){
        StudentResponse studentResponse = StudentResponse
                .builder()
                .name(studentEntity.getName())
                .id(studentEntity.getId())
                .city(studentEntity.getCity())
                .DOB(studentEntity.getDob())
                .rank(studentEntity.getRank())
                .build();
        studentResponse.setCreateAt(studentEntity.getCreateAt());
        studentResponse.setUpdateAt(studentEntity.getUpdateAt());
        return studentResponse;
    }
}
