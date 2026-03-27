package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.EstablishmentGrade;
import tn.esprit.etablissement.service.GradeService;
import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GradeController {

    private final GradeService service;

    @GetMapping("/establishment/{id}")
    public List<EstablishmentGrade> getByEstablishment(@PathVariable Long id) {
        return service.getByEstablishment(id);
    }

    @PostMapping("/establishment/{id}")
    public ResponseEntity<List<EstablishmentGrade>> saveGrades(
            @PathVariable Long id,
            @RequestBody List<EstablishmentGrade> grades) {
        return ResponseEntity.ok(service.saveGrades(id, grades));
    }

    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> delete(@PathVariable Long gradeId) {
        service.deleteGrade(gradeId);
        return ResponseEntity.noContent().build();
    }
}