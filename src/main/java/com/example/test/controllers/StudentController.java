package com.example.test.controllers;

import com.example.test.exceptions.ResourceNotFoundException;
import com.example.test.models.StudentEntity;
import com.example.test.responses.ApiResponse;
import com.example.test.responses.StudentListResponse;
import com.example.test.responses.StudentResponse;
import com.example.test.services.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("${api.prefix}/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse> getAll(){
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(studentService.getAllStudent())
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/pagingStudent/{page}")
    public ResponseEntity<StudentListResponse> paging(@PathVariable("page") int page,
                                                       @RequestParam("limit") int limit) {
        try{
            PageRequest pageRequest = PageRequest.of(
                    page, limit,
                    Sort.by("createAt").descending()
            );
            Page<StudentResponse> studentsResponsePage = studentService.getAllStudentsPage(pageRequest);
            int totalPage = studentsResponsePage.getTotalPages();
            List<StudentResponse> responseStudent = studentsResponsePage.getContent();
            return ResponseEntity.ok(StudentListResponse.builder()
                    .studentResponses(responseStudent)
                    .totalPages(totalPage)
                    .build());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/create/student")
    public ResponseEntity<?> create(@Valid @RequestBody StudentEntity student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors =bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(studentService.save(student))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update/student/{id}")
    public ResponseEntity<?> update( @PathVariable(name = "id") Long id,@Valid @RequestBody StudentEntity student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors =bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }else{
            StudentEntity studentEntity = studentService.getStudentById(id);
            if(student == null){
                throw new ResourceNotFoundException("Khong tim thay student voi id = "+id);
            }
            studentService.update(id,studentEntity);
            ApiResponse apiResponse = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Updated successfully")
                    .data(studentEntity)
                    .build();
            return ResponseEntity.ok(apiResponse);
        }

    }

    @DeleteMapping("/delete/student/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id){
        StudentEntity student = studentService.getStudentById(id);
        if(student == null){
            throw new ResourceNotFoundException("Khong tim thay student voi id = "+id);
        }
        studentService.delete(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Deleted successfully")
                .data(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
