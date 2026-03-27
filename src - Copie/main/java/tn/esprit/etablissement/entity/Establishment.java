package tn.esprit.etablissement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.etablissement.enums.EstablishmentStatus;
import java.time.LocalDateTime;

import java.util.List;


@Entity
@Table(name = "establishments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Establishment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    private String website;

    @Column(name = "logo_url")
    private String logoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstablishmentStatus status = EstablishmentStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @JsonIgnore
    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL)
    private List<Department> departments;
    @JsonIgnore
    @OneToMany(mappedBy = "establishment", cascade = CascadeType.ALL)
    private List<Room> rooms;

}
