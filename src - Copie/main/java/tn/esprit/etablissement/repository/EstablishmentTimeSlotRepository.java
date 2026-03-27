package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.EstablishmentTimeSlot;
import java.util.List;

public interface EstablishmentTimeSlotRepository
        extends JpaRepository<EstablishmentTimeSlot, Long> {

    List<EstablishmentTimeSlot> findByEstablishmentIdOrderBySlotOrder(
            Long establishmentId);

    void deleteByEstablishmentId(Long establishmentId);
}