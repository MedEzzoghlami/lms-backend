package tn.esprit.etablissement.entity;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.etablissement.enums.RoomType;

@Entity
@Table(name = "rooms")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomType type;

    @Column(nullable = false)
    private int capacity;

    private Integer floor;
    private String building;

    @Column(name = "has_projector")
    private Boolean hasProjector = false;

    @Column(name = "has_computers")
    private Boolean hasComputers = false;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @ManyToOne
    @JoinColumn(name = "establishment_id", nullable = false)
    private Establishment establishment;
}