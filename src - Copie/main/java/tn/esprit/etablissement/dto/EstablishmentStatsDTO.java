package tn.esprit.etablissement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstablishmentStatsDTO {
    private long totalStudents;
    private long activeClasses;
    private double averageFillRate;
    private long totalDepartments;
}