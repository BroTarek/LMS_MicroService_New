package com.lms.course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a Course in the LMS.
 * 
 * <p><b>OCL Constraints:</b></p>
 * <ul>
 *   <li>context Course inv TitleNotEmpty: self.title.size() > 0</li>
 *   <li>context Course inv HasTeacher: self.teacherUsername <> null and self.teacherUsername.size() > 0</li>
 * </ul>
 */
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "teacher_username", nullable = false)
    private String teacherUsername;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "duration_hours")
    private Integer durationHours = 0;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lesson> lessons = new ArrayList<>();
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Enrollment> enrollments = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
