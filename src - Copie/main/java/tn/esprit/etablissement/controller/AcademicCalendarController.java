package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.AcademicCalendar;
import tn.esprit.etablissement.service.AcademicCalendarService;
import java.util.List;

@RestController
@RequestMapping("/api/academic-calendar")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AcademicCalendarController {

    private final AcademicCalendarService service;

    @GetMapping("/establishment/{id}")
    public List<AcademicCalendar> getByEstablishment(
            @PathVariable Long id,
            @RequestParam(required = false) String academicYear) {
        if (academicYear != null) {
            return service.getByEstablishmentAndYear(id, academicYear);
        }
        return service.getByEstablishment(id);
    }

    @PostMapping("/establishment/{id}")
    public ResponseEntity<AcademicCalendar> create(
            @PathVariable Long id,
            @RequestBody AcademicCalendar period) {
        return ResponseEntity.ok(service.create(id, period));
    }

    @PutMapping("/{periodId}")
    public ResponseEntity<AcademicCalendar> update(
            @PathVariable Long periodId,
            @RequestBody AcademicCalendar period) {
        return ResponseEntity.ok(service.update(periodId, period));
    }

    @DeleteMapping("/{periodId}")
    public ResponseEntity<Void> delete(@PathVariable Long periodId) {
        service.delete(periodId);
        return ResponseEntity.noContent().build();
    }
}