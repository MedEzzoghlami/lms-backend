package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.EstablishmentGrade;
import java.util.List;

public interface EstablishmentGradeRepository
        extends JpaRepository<EstablishmentGrade, Long> {

    List<EstablishmentGrade> findByEstablishmentIdOrderByGradeOrder(
            Long establishmentId);

    void deleteByEstablishmentId(Long establishmentId);
}