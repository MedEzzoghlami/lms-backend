package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.Establishment;
import tn.esprit.etablissement.enums.EstablishmentStatus;
import java.util.List;

public interface EstablishmentRepository extends JpaRepository<Establishment, Long> {
    List<Establishment> findByStatus(EstablishmentStatus status);
    boolean existsByEmail(String email);
}