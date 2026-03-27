package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.SchoolClass;
import tn.esprit.etablissement.service.SchoolClassService;
import java.util.List;

@RestController
@RequestMapping("/api/classes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SchoolClassController {

    private final SchoolClassService service;

    @GetMapping("/by-department/{departmentId}")
    public List<SchoolClass> getByDepartment(@PathVariable Long departmentId) {
        return service.getByDepartment(departmentId);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SchoolClass> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(sc -> ResponseEntity.ok(sc))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/active/by-establishment/{establishmentId}")
    public List<SchoolClass> getActiveByEstablishment(@PathVariable Long establishmentId) {
        return service.getActiveByEstablishment(establishmentId);
    }

    @GetMapping("/active/by-establishment/{establishmentId}/level/{level}")
    public List<SchoolClass> getActiveByEstablishmentAndLevel(
            @PathVariable Long establishmentId,
            @PathVariable String level) {
        return service.getActiveByEstablishmentAndLevel(establishmentId, level);
    }

    @PostMapping("/department/{departmentId}")
    public ResponseEntity<SchoolClass> create(@PathVariable Long departmentId,
                                              @RequestBody SchoolClass sc) {
        return ResponseEntity.ok(service.create(departmentId, sc));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolClass> update(@PathVariable Long id,
                                              @RequestBody SchoolClass sc) {
        return ResponseEntity.ok(service.update(id, sc));
    }

    @PatchMapping("/{id}/increment-students")
    public ResponseEntity<Void> incrementStudents(@PathVariable Long id) {
        service.incrementStudentCount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}