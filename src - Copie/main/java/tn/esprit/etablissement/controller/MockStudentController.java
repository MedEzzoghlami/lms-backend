package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.MockStudent;
import tn.esprit.etablissement.entity.SchoolClass;
import tn.esprit.etablissement.service.MockStudentService;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mock-students")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MockStudentController {

    private final MockStudentService service;

    @GetMapping("/establishment/{id}")
    public List<MockStudent> getByEstablishment(@PathVariable Long id) {
        return service.getByEstablishment(id);
    }

    @GetMapping("/class/{classId}")
    public List<MockStudent> getByClass(@PathVariable Long classId) {
        return service.getByClass(classId);
    }

    @GetMapping("/unassigned")
    public List<MockStudent> getUnassigned(
            @RequestParam Long establishmentId,
            @RequestParam Long gradeId,
            @RequestParam Long departmentId) {
        return service.getUnassigned(establishmentId, gradeId, departmentId);
    }

    @PostMapping
    public ResponseEntity<MockStudent> create(@RequestBody MockStudent student) {
        return ResponseEntity.ok(service.save(student));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/auto-create-classes")
    public ResponseEntity<List<SchoolClass>> autoCreateClasses(
            @RequestBody Map<String, Object> body) {
        Long establishmentId = Long.valueOf(body.get("establishmentId").toString());
        Long gradeId = Long.valueOf(body.get("gradeId").toString());
        Long departmentId = Long.valueOf(body.get("departmentId").toString());
        String academicYear = body.get("academicYear").toString();
        String gradeName = body.get("gradeName").toString();
        return ResponseEntity.ok(service.autoCreateClasses(
                establishmentId, gradeId, departmentId, academicYear, gradeName));
    }
    @PatchMapping("/{studentId}/assign/{classId}")
    public ResponseEntity<MockStudent> assignToClass(
            @PathVariable Long studentId,
            @PathVariable Long classId) {
        return ResponseEntity.ok(service.assignToClass(studentId, classId));
    }

    @PatchMapping("/{studentId}/unassign")
    public ResponseEntity<MockStudent> unassignFromClass(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(service.unassignFromClass(studentId));
    }
    @GetMapping("/unassigned/by-grade")
    public List<MockStudent> getUnassignedByGrade(
            @RequestParam Long establishmentId,
            @RequestParam Long gradeId) {
        return service.getUnassignedByGrade(establishmentId, gradeId);
    }
}