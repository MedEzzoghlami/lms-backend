package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.dto.RattrapageDTO;
import tn.esprit.etablissement.entity.TimetableSlot;
import tn.esprit.etablissement.enums.Semester;
import tn.esprit.etablissement.service.EmailService;
import tn.esprit.etablissement.service.TimetableService;
import tn.esprit.etablissement.dto.TimetableGenerationResult;
import tn.esprit.etablissement.dto.UpdateSlotDTO;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/timetable")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TimetableController {

    private final TimetableService service;
    private final EmailService emailService;
    @PostMapping("/generate")
    public ResponseEntity<TimetableGenerationResult> generate(
            @RequestParam Long establishmentId,
            @RequestParam String level,
            @RequestParam Semester semester,
            @RequestParam String academicYear,
            @RequestParam String weekStart,
            @RequestParam(required = false) String notifyEmail,
            @RequestBody List<Long> courseIds) {

        LocalDate weekStartDate = LocalDate.parse(weekStart);
        TimetableGenerationResult result = service.generateTimetable(
                establishmentId, level, semester, academicYear, courseIds, weekStartDate);

        if (notifyEmail != null && !notifyEmail.isEmpty()
                && result.getScheduledCourses() > 0) {
            emailService.sendTimetableNotification(
                    notifyEmail, "Admin", weekStart, result);
        }

        return ResponseEntity.ok(result);
    }
    @GetMapping("/class/{classId}")
    public ResponseEntity<List<TimetableSlot>> getByClass(@PathVariable Long classId) {
        return ResponseEntity.ok(service.getSlotsByClass(classId));
    }

    @GetMapping("/teacher/{teacherId}")
    public ResponseEntity<List<TimetableSlot>> getByTeacher(@PathVariable Long teacherId) {
        return ResponseEntity.ok(service.getSlotsByTeacher(teacherId));
    }
    @PostMapping("/rattrapage")
    public ResponseEntity<TimetableSlot> addRattrapage(
            @RequestBody RattrapageDTO dto) {
        return ResponseEntity.ok(service.addRattrapage(dto));
    }
    @PutMapping("/slot/{slotId}")
    public ResponseEntity<TimetableSlot> updateSlot(
            @PathVariable Long slotId,
            @RequestBody UpdateSlotDTO dto) {
        return ResponseEntity.ok(service.updateSlot(slotId, dto));
    }

    @DeleteMapping("/slot/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId) {
        service.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/copy-week")
    public ResponseEntity<TimetableGenerationResult> copyWeek(
            @RequestParam Long establishmentId,
            @RequestParam String sourceWeekStart,
            @RequestParam String targetWeekStart,
            @RequestParam String academicYear,
            @RequestParam Semester semester) {

        LocalDate source = LocalDate.parse(sourceWeekStart);
        LocalDate target = LocalDate.parse(targetWeekStart);
        return ResponseEntity.ok(
                service.copyWeek(establishmentId, source, target,
                        academicYear, semester));
    }
    @GetMapping("/teacher/{teacherId}/classes")
    public ResponseEntity<List<Map<String, Object>>> getTeacherClasses(
            @PathVariable Long teacherId) {
        return ResponseEntity.ok(service.getTeacherClasses(teacherId));
    }

    @GetMapping("/teacher/{teacherId}/class/{classId}/courses")
    public ResponseEntity<List<Long>> getTeacherCoursesForClass(
            @PathVariable Long teacherId,
            @PathVariable Long classId) {
        return ResponseEntity.ok(
                service.getTeacherCoursesForClass(teacherId, classId));
    }
    @GetMapping("/class/{classId}/courses")
    public ResponseEntity<List<Long>> getCoursesByClass(
            @PathVariable Long classId) {
        return ResponseEntity.ok(service.getCoursesByClass(classId));
    }
    @GetMapping("/level/courses")
    public ResponseEntity<List<Long>> getCoursesByLevel(
            @RequestParam Long establishmentId,
            @RequestParam String level) {
        return ResponseEntity.ok(
                service.getCoursesByLevel(establishmentId, level));
    }

}