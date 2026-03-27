package tn.esprit.etablissement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "establishment_grades")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EstablishmentGrade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "grade_order", nullable = false)
    private Integer gradeOrder;

    @ManyToOne
    @JoinColumn(name = "establishment_id", nullable = false)
    private Establishment establishment;
}