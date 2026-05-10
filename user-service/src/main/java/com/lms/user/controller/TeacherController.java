package com.lms.user.controller;

import com.lms.user.dto.CourseSummary;
import com.lms.user.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    
    private final TeacherService teacherService;
    
    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseSummary>> getMyCourses(@RequestHeader("X-Username") String username) {
        return ResponseEntity.ok(teacherService.getMyCoursesWithTeacherNames(username));
    }
}
