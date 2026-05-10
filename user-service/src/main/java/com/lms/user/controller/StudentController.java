package com.lms.user.controller;

import com.lms.user.dto.CourseSummary;
import com.lms.user.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {
    
    private final StudentService studentService;
    
    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseSummary>> getMyCourses(@RequestHeader("X-Username") String username) {
       return ResponseEntity.ok(studentService.getMyCoursesWithTeacherNames(username));
    }
}
