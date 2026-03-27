package tn.esprit.etablissement.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.etablissement.enums.CalendarPeriodType;
import java.time.LocalDate;

@Entity
@Table(name = "academic_calendar_periods")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AcademicCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "establishment_id", nullable = false)
    private Establishment establishment;

    @Column(nullable = false)
    private String academicYear;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CalendarPeriodType periodType;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    private String description;
    private String color;
}