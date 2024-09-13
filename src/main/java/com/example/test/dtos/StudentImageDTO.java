package com.example.test.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class StudentImageDTO {
    @JsonProperty("student_id")
    @Min(value = 1,message = "Id của student phải lớn hơn 0")
    private Long studentId;

    @Size(min = 5,max = 200,message = "Tên của hình ảnh phải từ 5 kí tự , không quá hn 200 kí tự")
    @JsonProperty("image_url")
    private String imageUrl;
}
