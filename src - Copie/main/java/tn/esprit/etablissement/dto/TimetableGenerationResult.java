package tn.esprit.etablissement.dto;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class TimetableGenerationResult {
    private int scheduledCourses = 0;
    private int classesProcessed = 0;
    private List<ScheduledSlotInfo> scheduledSlots = new ArrayList<>();
    private List<UnscheduledCourseInfo> unscheduledCourses = new ArrayList<>();
    private List<String> skippedHolidays = new ArrayList<>();
    private List<String> weekDates = new ArrayList<>();

    @Data
    public static class ScheduledSlotInfo {
        private Long courseId;
        private Long teacherId;
        private String roomName;
        private String dayOfWeek;
        private String slotDate;
        private String startTime;
        private String endTime;
        private String className;
    }

    @Data
    public static class UnscheduledCourseInfo {
        private Long courseId;
        private String reason;
        private String detail;
    }
}