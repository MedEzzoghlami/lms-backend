package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.SchoolClass;
import java.util.List;

public interface SchoolClassRepository extends JpaRepository<SchoolClass, Long> {
    List<SchoolClass> findByDepartmentId(Long departmentId);
    List<SchoolClass> findByDepartmentEstablishmentIdAndLevelAndActiveTrue(Long establishmentId, String level);
    List<SchoolClass> findByDepartmentEstablishmentIdAndActiveTrue(Long establishmentId);

}