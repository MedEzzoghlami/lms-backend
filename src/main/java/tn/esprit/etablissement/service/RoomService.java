package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.entity.Room;
import tn.esprit.etablissement.repository.EstablishmentRepository;
import tn.esprit.etablissement.repository.RoomRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository repo;
    private final EstablishmentRepository establishmentRepo;

    public List<Room> getByEstablishment(Long establishmentId) {
        return repo.findByEstablishmentId(establishmentId);
    }

    public List<Room> getAvailableByEstablishment(Long establishmentId) {
        return repo.findByEstablishmentIdAndIsAvailableTrue(establishmentId);
    }

    public Room create(Long establishmentId, Room room) {
        Establishment est = establishmentRepo.findById(establishmentId)
                .orElseThrow(() -> new RuntimeException("Establishment not found"));
        room.setEstablishment(est);
        return repo.save(room);
    }

    public Room update(Long id, Room updated) {
        Room existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found: " + id));
        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setCapacity(updated.getCapacity());
        existing.setFloor(updated.getFloor());
        existing.setBuilding(updated.getBuilding());
        existing.setHasProjector(updated.getHasProjector());
        existing.setHasComputers(updated.getHasComputers());
        return repo.save(existing);
    }

    public void toggleAvailability(Long id, boolean available) {
        Room room = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found: " + id));
        room.setIsAvailable(available);
        repo.save(room);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}