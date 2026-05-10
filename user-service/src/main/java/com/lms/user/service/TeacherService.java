package com.lms.user.service;

import com.lms.user.dto.CourseSummary;
import com.lms.user.client.CourseServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final CourseServiceClient courseServiceClient;
    private final UserProfileService userProfileService;

    public List<CourseSummary> getMyCoursesWithTeacherNames(String username) {
        List<CourseSummary> courses = courseServiceClient.getCoursesByTeacher(username);
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
