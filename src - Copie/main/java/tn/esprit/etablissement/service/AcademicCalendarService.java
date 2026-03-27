package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.entity.AcademicCalendar;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.repository.AcademicCalendarRepository;
import tn.esprit.etablissement.repository.EstablishmentRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AcademicCalendarService {

    private final AcademicCalendarRepository repo;
    private final EstablishmentRepository establishmentRepo;

    public List<AcademicCalendar> getByEstablishment(Long establishmentId) {
        return repo.findByEstablishmentId(establishmentId);
    }

    public List<AcademicCalendar> getByEstablishmentAndYear(
            Long establishmentId, String academicYear) {
        return repo.findByEstablishmentIdAndAcademicYear(
                establishmentId, academicYear);
    }

    public AcademicCalendar create(Long establishmentId,
                                   AcademicCalendar period) {
        Establishment est = establishmentRepo.findById(establishmentId)
                .orElseThrow(() -> new RuntimeException("Establishment not found"));
        period.setEstablishment(est);
        return repo.save(period);
    }

    public AcademicCalendar update(Long id, AcademicCalendar updated) {
        AcademicCalendar existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Period not found"));
        existing.setName(updated.getName());
        existing.setPeriodType(updated.getPeriodType());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());
        existing.setDescription(updated.getDescription());
        existing.setColor(updated.getColor());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}