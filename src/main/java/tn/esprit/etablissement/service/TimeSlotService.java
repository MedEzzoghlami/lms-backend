package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.entity.EstablishmentTimeSlot;
import tn.esprit.etablissement.repository.EstablishmentRepository;
import tn.esprit.etablissement.repository.EstablishmentTimeSlotRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeSlotService {

    private final EstablishmentTimeSlotRepository repo;
    private final EstablishmentRepository establishmentRepo;

    public List<EstablishmentTimeSlot> getByEstablishment(Long establishmentId) {
        return repo.findByEstablishmentIdOrderBySlotOrder(establishmentId);
    }

    @Transactional
    public List<EstablishmentTimeSlot> saveSlots(
            Long establishmentId,
            List<EstablishmentTimeSlot> slots) {

        Establishment est = establishmentRepo.findById(establishmentId)
                .orElseThrow(() -> new RuntimeException("Establishment not found"));

        repo.deleteByEstablishmentId(establishmentId);

        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).setEstablishment(est);
            slots.get(i).setSlotOrder(i + 1);
        }

        return repo.saveAll(slots);
    }

    public void deleteSlot(Long slotId) {
        repo.deleteById(slotId);
    }
}