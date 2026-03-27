package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.TeacherCourse;
import java.util.List;

public interface TeacherCourseRepository extends JpaRepository<TeacherCourse, Long> {
    List<TeacherCourse> findByCourseId(Long courseId);
    List<TeacherCourse> findByTeacherId(Long teacherId);
    boolean existsByTeacherIdAndCourseId(Long teacherId, Long courseId);
}