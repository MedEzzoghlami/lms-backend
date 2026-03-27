package tn.esprit.etablissement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.esprit.etablissement.entity.TimetableSlot;
import tn.esprit.etablissement.enums.DayOfWeek;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TimetableSlotRepository extends JpaRepository<TimetableSlot, Long> {
    List<TimetableSlot> findByTimetableId(Long timetableId);
    List<TimetableSlot> findByTeacherId(Long teacherId);

    boolean existsByRoomIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            Long roomId, DayOfWeek day, LocalTime endTime, LocalTime startTime);

    boolean existsByTeacherIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
            Long teacherId, DayOfWeek day, LocalTime endTime, LocalTime startTime);

    long countByTeacherIdAndTimetableAcademicYear(Long teacherId, String academicYear);
    @Query("SELECT COUNT(ts) FROM TimetableSlot ts WHERE ts.teacherId = :teacherId AND ts.slotDate >= :weekStart AND ts.slotDate <= :weekEnd")
    long countByTeacherIdAndWeek(@Param("teacherId") Long teacherId,
                                 @Param("weekStart") LocalDate weekStart,
                                 @Param("weekEnd") LocalDate weekEnd);
    @Query("SELECT COUNT(ts) FROM TimetableSlot ts WHERE ts.room.id = :roomId AND ts.slotDate >= :weekStart AND ts.slotDate <= :weekEnd AND ts.dayOfWeek = :dayOfWeek AND ts.startTime < :endTime AND ts.endTime > :startTime")
    long countRoomConflictsInWeek(@Param("roomId") Long roomId,
                                  @Param("weekStart") LocalDate weekStart,
                                  @Param("weekEnd") LocalDate weekEnd,
                                  @Param("dayOfWeek") DayOfWeek dayOfWeek,
                                  @Param("startTime") LocalTime startTime,
                                  @Param("endTime") LocalTime endTime);
    List<TimetableSlot> findBySlotDateBetweenAndTimetable_SchoolClass_Department_Establishment_Id(
            LocalDate start, LocalDate end, Long establishmentId);
}