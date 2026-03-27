package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.Department;
import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByEstablishmentId(Long establishmentId);
    boolean existsByCodeAndEstablishmentId(String code, Long establishmentId);
}