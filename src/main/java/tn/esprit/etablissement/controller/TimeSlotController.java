package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.EstablishmentTimeSlot;
import tn.esprit.etablissement.service.TimeSlotService;
import java.util.List;

@RestController
@RequestMapping("/api/time-slots")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TimeSlotController {

    private final TimeSlotService service;

    @GetMapping("/establishment/{id}")
    public List<EstablishmentTimeSlot> getByEstablishment(@PathVariable Long id) {
        return service.getByEstablishment(id);
    }

    @PostMapping("/establishment/{id}")
    public ResponseEntity<List<EstablishmentTimeSlot>> saveSlots(
            @PathVariable Long id,
            @RequestBody List<EstablishmentTimeSlot> slots) {
        return ResponseEntity.ok(service.saveSlots(id, slots));
    }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> delete(@PathVariable Long slotId) {
        service.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}