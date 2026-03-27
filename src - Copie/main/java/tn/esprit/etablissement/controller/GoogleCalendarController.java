package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.entity.TimetableSlot;
import tn.esprit.etablissement.repository.TimetableRepository;
import tn.esprit.etablissement.repository.TimetableSlotRepository;
import tn.esprit.etablissement.service.GoogleCalendarService;

import java.util.*;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GoogleCalendarController {

    private final GoogleCalendarService calendarService;
    private final TimetableSlotRepository slotRepo;
    private final TimetableRepository timetableRepo;

    @PostMapping("/export/class/{classId}")
    public ResponseEntity<Map<String, Object>> exportClassToCalendar(
            @PathVariable Long classId,
            @RequestParam(defaultValue = "primary") String calendarId) {
        try {
            List<tn.esprit.etablissement.entity.Timetable> timetables =
                    timetableRepo.findBySchoolClassId(classId);

            List<TimetableSlot> slots = new ArrayList<>();
            for (var t : timetables) {
                slots.addAll(slotRepo.findByTimetableId(t.getId()));
            }

            if (slots.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "success", false,
                        "message", "No slots found for this class."
                ));
            }

            List<String> eventIds = calendarService
                    .exportSlotsToCalendar(slots, calendarId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", eventIds.size() + " events created in Google Calendar.",
                    "eventIds", eventIds
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "success", false,
                    "message", "Calendar export failed: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/auth-url")
    public ResponseEntity<Map<String, String>> getAuthUrl() {
        try {
            String url = calendarService.getAuthUrl();
            return ResponseEntity.ok(Map.of("authUrl", url));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                    Map.of("error", e.getMessage()));
        }
    }
}