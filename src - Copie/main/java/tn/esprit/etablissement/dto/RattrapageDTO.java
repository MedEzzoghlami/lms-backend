package tn.esprit.etablissement.dto;

import lombok.Data;
import tn.esprit.etablissement.enums.DayOfWeek;
import tn.esprit.etablissement.enums.SessionType;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RattrapageDTO {
    private Long classId;
    private Long courseId;
    private Long teacherId;
    private Long roomId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private SessionType sessionType;
    private String academicYear;
    private String semester;
}