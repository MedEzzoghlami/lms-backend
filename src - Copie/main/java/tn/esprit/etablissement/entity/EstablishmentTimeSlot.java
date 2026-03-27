package tn.esprit.etablissement.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "establishment_time_slots")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EstablishmentTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "establishment_id", nullable = false)
    private Establishment establishment;

    @Column(name = "slot_order", nullable = false)
    private Integer slotOrder;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "label")
    private String label;
}