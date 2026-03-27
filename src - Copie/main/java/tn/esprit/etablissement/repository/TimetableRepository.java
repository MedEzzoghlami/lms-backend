package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.Timetable;
import tn.esprit.etablissement.enums.Semester;
import java.util.List;
import java.util.Optional;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findBySchoolClassId(Long classId);
    Optional<Timetable> findBySchoolClassIdAndSemesterAndAcademicYear(Long classId, Semester semester, String academicYear);
}