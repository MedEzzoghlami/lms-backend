package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.MockStudent;
import java.util.List;

public interface MockStudentRepository
        extends JpaRepository<MockStudent, Long> {

    List<MockStudent> findByEstablishmentId(Long establishmentId);

    List<MockStudent> findByEstablishmentIdAndGradeIdAndDepartmentId(
            Long establishmentId, Long gradeId, Long departmentId);

    List<MockStudent> findByClassId(Long classId);

    long countByClassId(Long classId);
    List<MockStudent> findByEstablishmentIdAndGradeIdAndClassIdIsNull(
            Long establishmentId, Long gradeId);
}