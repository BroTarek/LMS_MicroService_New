package com.lms.course.service;

import com.lms.course.dto.CreateLessonRequest;
import com.lms.course.entity.Course;
import com.lms.course.entity.Lesson;
import com.lms.course.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    
    private final LessonRepository lessonRepository;
    private final CourseService courseService;
    
    @Transactional
    public Lesson addLesson(Long courseId, CreateLessonRequest request, String teacherUsername) {
        Course course = courseService.getCourse(courseId);
        if (!course.getTeacherUsername().equals(teacherUsername)) {
            throw new RuntimeException("You are not the owner of this course");
        }
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(request.getTitle());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setOrderIndex(request.getOrderIndex());
        
        // The Trick: random number * material size (in MB)
        if (request.getFileSize() != null && request.getFileSize() > 0) {
            double sizeInMb = request.getFileSize() / 1024.0 / 1024.0;
            int extraDuration = (int) Math.ceil(sizeInMb * Math.random() * 5); // up to 5 hours per MB randomly
            // Ensure at least 1 hour is added if there is a file
            if (extraDuration == 0) extraDuration = 1;
            
            Integer currentDuration = course.getDurationHours();
            if (currentDuration == null) currentDuration = 0;
            
            course.setDurationHours(currentDuration + extraDuration);
            // courseRepository.save(course) is not strictly needed if Course is already managed, 
            // but we rely on Cascade or EntityManager dirty checking. We'll save it to be safe.
            // Wait, we don't have courseRepository here. But it's @Transactional, so it will be saved!
        }
        
        return lessonRepository.save(lesson);
    }
    
    public List<Lesson> getLessonsByCourse(Long courseId) {
        return lessonRepository.findByCourseIdOrderByOrderIndex(courseId);
    }
    
    public Lesson getLesson(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found: " + lessonId));
    }

    @Transactional
    public Lesson updateLesson(Long courseId, Long lessonId, CreateLessonRequest request, String teacherUsername) {
        Course course = courseService.getCourse(courseId);
        if (!course.getTeacherUsername().equals(teacherUsername)) {
            throw new RuntimeException("You are not the owner of this course");
        }
        Lesson lesson = getLesson(lessonId);
        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new RuntimeException("Lesson does not belong to this course");
        }
        lesson.setTitle(request.getTitle());
        lesson.setContentUrl(request.getContentUrl());
        lesson.setOrderIndex(request.getOrderIndex());
        return lessonRepository.save(lesson);
    }

    @Transactional
    public void deleteLesson(Long courseId, Long lessonId, String teacherUsername, String role) {
        Course course = courseService.getCourse(courseId);
        if (!"ADMIN".equals(role) && !course.getTeacherUsername().equals(teacherUsername)) {
            throw new RuntimeException("You are not the owner of this course");
        }
        Lesson lesson = getLesson(lessonId);
        if (!lesson.getCourse().getId().equals(courseId)) {
            throw new RuntimeException("Lesson does not belong to this course");
        }
        lessonRepository.delete(lesson);
    }
}
