package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.TeacherCourse;
import tn.esprit.etablissement.repository.TeacherCourseRepository;
import java.util.List;

@RestController
@RequestMapping("/api/teacher-courses")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TeacherCourseController {

    private final TeacherCourseRepository repo;
    private final TeacherCourseRepository teacherCourseRepo;

    @GetMapping("/by-course/{courseId}")
    public List<TeacherCourse> getByCourse(@PathVariable Long courseId) {
        return repo.findByCourseId(courseId);
    }

    @GetMapping("/by-teacher/{teacherId}")
    public List<TeacherCourse> getByTeacher(@PathVariable Long teacherId) {
        return repo.findByTeacherId(teacherId);
    }

    @PostMapping
    public ResponseEntity<TeacherCourse> assign(@RequestBody TeacherCourse tc) {
        if (repo.existsByTeacherIdAndCourseId(tc.getTeacherId(), tc.getCourseId()))
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(repo.save(tc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public List<TeacherCourse> getAll() {
        return teacherCourseRepo.findAll();
    }
}