package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tn.esprit.etablissement.enums.DayOfWeek;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PublicHolidayService {

    private final RestTemplate restTemplate;

    private static final String API_URL =
            "https://date.nager.at/api/v3/PublicHolidays/{year}/{country}";

    private final Map<String, Set<LocalDate>> cache = new HashMap<>();

    public Set<LocalDate> getHolidays(int year, String countryCode) {
        String key = year + "_" + countryCode;
        if (cache.containsKey(key)) return cache.get(key);

        try {
            Map[] response = restTemplate.getForObject(
                    API_URL, Map[].class,
                    Map.of("year", year, "country", countryCode)
            );

            Set<LocalDate> holidays = new HashSet<>();
            if (response != null) {
                for (Map h : response) {
                    String dateStr = (String) h.get("date");
                    holidays.add(LocalDate.parse(dateStr));
                }
            }
            cache.put(key, holidays);
            return holidays;

        } catch (Exception e) {
            System.out.println("Could not fetch holidays: " + e.getMessage());
            return new HashSet<>();
        }
    }

    public boolean isHoliday(LocalDate date, String countryCode) {
        return getHolidays(date.getYear(), countryCode).contains(date);
    }

    public String getHolidayName(LocalDate date, String countryCode) {
        try {
            Map[] response = restTemplate.getForObject(
                    API_URL, Map[].class,
                    Map.of("year", date.getYear(), "country", countryCode)
            );
            if (response != null) {
                for (Map h : response) {
                    if (date.toString().equals(h.get("date"))) {
                        return (String) h.get("localName");
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public DayOfWeek toDayOfWeek(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> DayOfWeek.MONDAY;
            case TUESDAY -> DayOfWeek.TUESDAY;
            case WEDNESDAY -> DayOfWeek.WEDNESDAY;
            case THURSDAY -> DayOfWeek.THURSDAY;
            case FRIDAY -> DayOfWeek.FRIDAY;
            default -> null;
        };
    }

    public List<LocalDate> getWorkingDays(
            LocalDate startDate, int numberOfDays, String countryCode) {

        List<LocalDate> workingDays = new ArrayList<>();
        LocalDate current = startDate;

        while (workingDays.size() < numberOfDays) {
            java.time.DayOfWeek dow = current.getDayOfWeek();
            boolean isWeekend = dow == java.time.DayOfWeek.SATURDAY
                    || dow == java.time.DayOfWeek.SUNDAY;

            if (!isWeekend && !isHoliday(current, countryCode)) {
                workingDays.add(current);
            }
            current = current.plusDays(1);
        }
        return workingDays;
    }
}