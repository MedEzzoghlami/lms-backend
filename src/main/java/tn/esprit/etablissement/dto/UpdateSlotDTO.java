package tn.esprit.etablissement.dto;

import lombok.Data;
import tn.esprit.etablissement.enums.DayOfWeek;
import tn.esprit.etablissement.enums.SessionCategory;
import tn.esprit.etablissement.enums.SessionType;
import java.time.LocalTime;

@Data
public class UpdateSlotDTO {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private SessionType sessionType;
    private Long teacherId;
    private RoomRef room;
    private SessionCategory sessionCategory;
    @Data
    public static class RoomRef {
        private Long id;
    }
}