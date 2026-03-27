package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.entity.Department;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.repository.DepartmentRepository;
import tn.esprit.etablissement.repository.EstablishmentRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository repo;
    private final EstablishmentRepository establishmentRepo;

    public List<Department> getByEstablishment(Long establishmentId) {
        return repo.findByEstablishmentId(establishmentId);
    }

    public Department create(Long establishmentId, Department d) {
        if (repo.existsByCodeAndEstablishmentId(d.getCode(), establishmentId))
            throw new RuntimeException("Code already exists in this establishment: " + d.getCode());
        Establishment est = establishmentRepo.findById(establishmentId)
                .orElseThrow(() -> new RuntimeException("Establishment not found"));
        d.setEstablishment(est);
        return repo.save(d);
    }

    public Department update(Long id, Department updated) {
        Department existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found: " + id));
        existing.setName(updated.getName());
        existing.setCode(updated.getCode());
        existing.setDescription(updated.getDescription());
        existing.setHeadUserId(updated.getHeadUserId());
        return repo.save(existing);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}