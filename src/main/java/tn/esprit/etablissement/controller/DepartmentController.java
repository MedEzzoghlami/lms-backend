package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.Department;
import tn.esprit.etablissement.repository.DepartmentRepository;
import tn.esprit.etablissement.service.DepartmentService;
import java.util.List;

@RestController
@RequestMapping("/api/departments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentService service;
    private final DepartmentRepository departmentRepo;
    @GetMapping("/by-establishment/{establishmentId}")
    public List<Department> getByEstablishment(@PathVariable Long establishmentId) {
        return service.getByEstablishment(establishmentId);
    }

    @PostMapping("/establishment/{establishmentId}")
    public ResponseEntity<Department> create(@PathVariable Long establishmentId,
                                             @RequestBody Department d) {
        return ResponseEntity.ok(service.create(establishmentId, d));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> update(@PathVariable Long id,
                                             @RequestBody Department d) {
        return ResponseEntity.ok(service.update(id, d));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable Long id) {
        return departmentRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}