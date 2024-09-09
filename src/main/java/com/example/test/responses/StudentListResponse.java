package com.example.test.responses;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

public class StudentListResponse {
    private List<StudentResponse> studentResponses;
    private int totalPages;
}

