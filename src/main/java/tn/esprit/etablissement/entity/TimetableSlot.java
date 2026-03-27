package tn.esprit.etablissement.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.etablissement.enums.DayOfWeek;
import tn.esprit.etablissement.enums.SessionCategory;
import tn.esprit.etablissement.enums.SessionType;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "timetable_slots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TimetableSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "session_category")
    @Enumerated(EnumType.STRING)
    private SessionCategory sessionCategory = SessionCategory.NORMAL;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "session_type", nullable = false)
    private SessionType sessionType;

    @ManyToOne
    @JoinColumn(name = "timetable_id", nullable = false)
    private Timetable timetable;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "slot_date")
    private LocalDate slotDate;
}