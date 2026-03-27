package tn.esprit.etablissement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teacher_courses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TeacherCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Column(name = "course_id", nullable = false)
    private Long courseId;
}