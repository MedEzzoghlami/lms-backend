package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.etablissement.entity.AcademicCalendar;
import java.util.List;

public interface AcademicCalendarRepository
        extends JpaRepository<AcademicCalendar, Long> {

    List<AcademicCalendar> findByEstablishmentIdAndAcademicYear(
            Long establishmentId, String academicYear);

    List<AcademicCalendar> findByEstablishmentId(Long establishmentId);
}