package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.Room;
import tn.esprit.etablissement.repository.RoomRepository;
import tn.esprit.etablissement.service.RoomService;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RoomController {

    private final RoomService service;
    private final RoomRepository roomRepo;
    @GetMapping("/by-establishment/{establishmentId}")
    public List<Room> getByEstablishment(@PathVariable Long establishmentId) {
        return service.getByEstablishment(establishmentId);
    }

    @GetMapping("/available/by-establishment/{establishmentId}")
    public List<Room> getAvailable(@PathVariable Long establishmentId) {
        return service.getAvailableByEstablishment(establishmentId);
    }

    @PostMapping("/establishment/{establishmentId}")
    public ResponseEntity<Room> create(@PathVariable Long establishmentId,
                                       @RequestBody Room room) {
        return ResponseEntity.ok(service.create(establishmentId, room));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> update(@PathVariable Long id,
                                       @RequestBody Room room) {
        return ResponseEntity.ok(service.update(id, room));
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<Void> toggleAvailability(@PathVariable Long id,
                                                   @RequestParam boolean available) {
        service.toggleAvailability(id, available);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Room> getById(@PathVariable Long id) {
        return roomRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}