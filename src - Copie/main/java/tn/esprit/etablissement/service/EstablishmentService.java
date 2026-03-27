package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.entity.SchoolClass;
import tn.esprit.etablissement.enums.EstablishmentStatus;
import tn.esprit.etablissement.repository.EstablishmentRepository;
import java.util.List;
import tn.esprit.etablissement.dto.EstablishmentStatsDTO;
import tn.esprit.etablissement.repository.SchoolClassRepository;
import tn.esprit.etablissement.repository.DepartmentRepository;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

    private final EstablishmentRepository repo;

    public List<Establishment> getAll() {
        return repo.findAll();
    }

    public Establishment getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Establishment not found: " + id));
    }

    public Establishment create(Establishment e) {
        if (repo.existsByEmail(e.getEmail()))
            throw new RuntimeException("Email already used: " + e.getEmail());
        return repo.save(e);
    }

    public Establishment update(Long id, Establishment updated) {
        Establishment existing = getById(id);
        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        existing.setPhone(updated.getPhone());
        existing.setAddress(updated.getAddress());
        existing.setCity(updated.getCity());
        existing.setCountry(updated.getCountry());
        existing.setWebsite(updated.getWebsite());
        existing.setLogoUrl(updated.getLogoUrl());
        return repo.save(existing);
    }

    public void updateStatus(Long id, EstablishmentStatus status) {
        Establishment e = getById(id);
        e.setStatus(status);
        repo.save(e);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
    private final SchoolClassRepository classRepo;
    private final DepartmentRepository departmentRepo;
    public EstablishmentStatsDTO getStats(Long establishmentId) {
        List<SchoolClass> activeClasses = classRepo
                .findByDepartmentEstablishmentIdAndActiveTrue(establishmentId);

        long totalStudents = activeClasses.stream()
                .mapToLong(SchoolClass::getCurrentStudents).sum();

        double avgFillRate = activeClasses.isEmpty() ? 0 :
                activeClasses.stream()
                        .mapToDouble(c -> (double) c.getCurrentStudents() / c.getMaxStudents() * 100)
                        .average().orElse(0);

        long totalDepartments = departmentRepo.findByEstablishmentId(establishmentId).size();

        return new EstablishmentStatsDTO(
                totalStudents,
                activeClasses.size(),
                Math.round(avgFillRate * 10.0) / 10.0,
                totalDepartments
        );
    }
}