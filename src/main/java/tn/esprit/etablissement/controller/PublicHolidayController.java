package tn.esprit.etablissement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.etablissement.service.PublicHolidayService;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/holidays")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PublicHolidayController {

    private final PublicHolidayService service;

    @GetMapping("/{year}/{country}")
    public Set<String> getHolidays(
            @PathVariable int year,
            @PathVariable String country) {
        return service.getHolidays(year, country)
                .stream()
                .map(LocalDate::toString)
                .collect(java.util.stream.Collectors.toSet());
    }

    @GetMapping("/check")
    public Map<String, Object> checkDate(
            @RequestParam String date,
            @RequestParam String country) {
        LocalDate d = LocalDate.parse(date);
        boolean isHoliday = service.isHoliday(d, country);
        String name = isHoliday ? service.getHolidayName(d, country) : null;
        return Map.of(
                "date", date,
                "isHoliday", isHoliday,
                "holidayName", name != null ? name : ""
        );
    }
}