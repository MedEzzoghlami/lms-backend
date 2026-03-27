package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.entity.EstablishmentGrade;
import tn.esprit.etablissement.repository.EstablishmentGradeRepository;
import tn.esprit.etablissement.repository.EstablishmentRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final EstablishmentGradeRepository repo;
    private final EstablishmentRepository establishmentRepo;

    public List<EstablishmentGrade> getByEstablishment(Long establishmentId) {
        return repo.findByEstablishmentIdOrderByGradeOrder(establishmentId);
    }

    @Transactional
    public List<EstablishmentGrade> saveGrades(
            Long establishmentId, List<EstablishmentGrade> grades) {

        Establishment est = establishmentRepo.findById(establishmentId)
                .orElseThrow(() -> new RuntimeException("Establishment not found"));

        repo.deleteByEstablishmentId(establishmentId);

        for (int i = 0; i < grades.size(); i++) {
            grades.get(i).setEstablishment(est);
            grades.get(i).setGradeOrder(i + 1);
        }
        return repo.saveAll(grades);
    }

    public void deleteGrade(Long gradeId) {
        repo.deleteById(gradeId);
    }
}