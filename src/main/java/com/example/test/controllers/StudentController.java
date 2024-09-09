package com.example.test.controllers;

import com.example.test.models.StudentEntity;
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
@RequestMapping("${api.prefix}/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/pagingStudent/{page}")
    public ResponseEntity<StudentListResponse> paging(@PathVariable("page") int page,
                                                       @RequestParam("limit") int limit) {
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
    }

    @PostMapping("/create/student")
    public ResponseEntity<?> create(@Valid @RequestBody StudentEntity student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors =bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(studentService.save(student), HttpStatus.OK);
    }

    @PutMapping("/update/student/{id}")
    public ResponseEntity<?> update( @PathVariable(name = "id") Long id,@Valid @RequestBody StudentEntity student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors =bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(studentService.update(id,student), HttpStatus.OK);
    }

    @DeleteMapping("/delete/student/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id){
        studentService.delete(id);
        return ResponseEntity.ok().body("Delete SuccessFull");
    }
}
