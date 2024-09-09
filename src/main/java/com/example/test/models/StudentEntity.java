package com.example.test.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "student")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên không được trống.")
    @Column(name = "name")
    private String name;

    @NotBlank(message = "Thành phố không được trống.")
    @Column(name = "city")
    private String city;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @Past(message = "Phải 1 ngày trong quá khứ")
    @Column(name = "dob")
    private LocalDate dob;


    @Enumerated(EnumType.STRING)
    @NotNull(message = "Xếp loại không được trống")
    @Column(name = "student_rank")
    private Rank rank;
}
