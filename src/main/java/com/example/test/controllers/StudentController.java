package com.example.test.controllers;

import com.example.test.dtos.StudentImageDTO;
import com.example.test.exceptions.ResourceNotFoundException;
import com.example.test.models.Rank;
import com.example.test.models.StudentEntity;
import com.example.test.models.StudentImage;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                    Sort.by("createAt").ascending()
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

    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchPaging(@PathVariable("name") String name,@RequestParam("page") int page,@RequestParam("limit") int limit){
        try{
            PageRequest pageRequest = PageRequest.of(page,limit,Sort.by("createAt").ascending());
            Page<StudentResponse> studentResponses = studentService.searchStudents(name,pageRequest);
            int totalPage = studentResponses.getTotalPages();
            List<StudentResponse> responseList = studentResponses.getContent();
            return ResponseEntity.ok((StudentListResponse.builder()
                    .studentResponses(responseList)
                    .totalPages(totalPage)
                    .build()));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filterPaging(@RequestParam("rank")Rank rank, @RequestParam("dob")int dob, @RequestParam("page") int page, @RequestParam("limit") int limit){
        try{
            PageRequest pageRequest = PageRequest.of(page,limit,Sort.by("createAt").ascending());
            Page<StudentResponse> studentResponses = studentService.filter(rank,dob,pageRequest);
            int totalPage = studentResponses.getTotalPages();
            List<StudentResponse> responseList = studentResponses.getContent();
            return ResponseEntity.ok((StudentListResponse.builder()
                    .studentResponses(responseList)
                    .totalPages(totalPage)
                    .build()));
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAllImage/{id}")
    public ResponseEntity<ApiResponse> getAllImage(@PathVariable Long id){
        ApiResponse apiResponse = ApiResponse.builder()
                .data(studentService.getAllStudentImages(id))
                .status(HttpStatus.OK.value())
                .message("Get successfully")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> uploads(@PathVariable Long id, @ModelAttribute("files") List<MultipartFile> files) throws IOException{
        List<StudentImage> studentImages = new ArrayList<>();
        int count = 0;
        for (MultipartFile file:files ){
            if(file!= null){
                if(file.getSize()==0){
                    count++;
                    continue;
                }
                String fileName = storeFile(file);
                StudentImageDTO studentImageDTO = StudentImageDTO.builder()
                        .imageUrl(fileName)
                        .build();
                StudentImage studentImage = studentService.saveStudentImage(id,studentImageDTO);
                studentImages.add(studentImage);
            }
        }
        if(count == 1){
            throw new IllegalArgumentException("File chua duoc chon");
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Upload successfully")
                .data(studentImages)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody StudentEntity student, BindingResult bindingResult){
        ApiResponse apiResponse  = null;
        if(bindingResult.hasErrors()){
            List<String> errors =bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            apiResponse = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Error")
                    .data(errors)
                    .build();
            return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
        }
        apiResponse = ApiResponse.builder()
                .status(HttpStatus.OK.value())
                .message("OK")
                .data(studentService.save(student))
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update( @PathVariable(name = "id") Long id,@Valid @RequestBody StudentEntity student, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            List<String> errors =bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage).toList();
            return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
        }else{
            StudentEntity studentEntity = studentService.getStudentById(id);
            if(studentEntity == null){
                throw new ResourceNotFoundException("Khong tim thay student voi id = "+id);
            }
            studentService.update(id,student);
            ApiResponse apiResponse = ApiResponse.builder()
                    .status(HttpStatus.OK.value())
                    .message("Updated successfully")
                    .data(studentEntity)
                    .build();
            return ResponseEntity.ok(apiResponse);
        }

    }

    @DeleteMapping("/delete/{id}")
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

    private String storeFile(MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueFileName = UUID.randomUUID().toString()+"_"+filename;
        java.nio.file.Path uploadDdir = Paths.get("upload");
        if(!Files.exists(uploadDdir)){
            Files.createDirectories(uploadDdir);
        }
        java.nio.file.Path destination = Paths.get(uploadDdir.toString(),uniqueFileName);
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
}
