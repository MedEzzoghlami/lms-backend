package tn.esprit.etablissement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "mock_students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MockStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(name = "establishment_id", nullable = false)
    private Long establishmentId;

    @Column(name = "level")
    private String level;

    @Column(name = "grade_id", nullable = false)
    private Long gradeId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "class_id")
    private Long classId;
}