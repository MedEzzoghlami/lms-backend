package tn.esprit.etablissement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "classes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SchoolClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @Column(name = "grade_id")
    private Long gradeId;

    @Column(nullable = false)
    private String level;

    @Column(name = "max_students")
    private Integer maxStudents = 25;

    @Column(name = "current_students")
    private Integer currentStudents = 0;

    @Column(name = "is_active")
    private Boolean active = true;
    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @JsonIgnore
    @OneToMany(mappedBy = "schoolClass", cascade = CascadeType.ALL)
    private List<Timetable> timetables;
}