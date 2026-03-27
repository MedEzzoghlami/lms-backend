package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.dto.EstablishmentStatsDTO;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.enums.EstablishmentStatus;
import tn.esprit.etablissement.service.EstablishmentService;
import java.util.List;

@RestController
@RequestMapping("/api/establishments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EstablishmentController {

    private final EstablishmentService service;

    @GetMapping
    public List<Establishment> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Establishment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<Establishment> create(@RequestBody Establishment e) {
        return ResponseEntity.ok(service.create(e));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Establishment> update(@PathVariable Long id,
                                                @RequestBody Establishment e) {
        return ResponseEntity.ok(service.update(id, e));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id,
                                             @RequestParam EstablishmentStatus status) {
        service.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<EstablishmentStatsDTO> getStats(@PathVariable Long id) {
        return ResponseEntity.ok(service.getStats(id));
    }
}