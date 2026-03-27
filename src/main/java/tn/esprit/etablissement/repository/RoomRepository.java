package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.Room;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByEstablishmentId(Long establishmentId);
    List<Room> findByEstablishmentIdAndIsAvailableTrue(Long establishmentId);
    List<Room> findByEstablishmentIdAndCapacityGreaterThanEqual(Long establishmentId, int capacity);
}