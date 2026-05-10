package com.lms.user.service;


import com.lms.user.client.CourseServiceClient;

import com.lms.user.dto.CourseSummary;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final CourseServiceClient courseServiceClient;
    private final UserProfileService userProfileService;

    public List<CourseSummary> getMyCoursesWithTeacherNames(String username) {
        List<CourseSummary> courses = courseServiceClient.getCoursesByStudent(username);
        courses.forEach(course -> {
            if (course.getTeacherUsername() != null) {
                try {
                    course.setTeacherName(userProfileService.getProfile(course.getTeacherUsername()).getFullName());
                } catch (Exception e) {
                    course.setTeacherName("Unknown Teacher");
                }
            }
        });
        return courses;
    }
}
