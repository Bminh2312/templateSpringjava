package com.example.test.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_image")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class StudentImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentEntity student;

    @Column(name = "image_url", length = 300)
    private String imageUrl;
}
