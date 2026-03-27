package tn.esprit.etablissement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.dto.RattrapageDTO;
import tn.esprit.etablissement.dto.TimetableGenerationResult;
import tn.esprit.etablissement.dto.UpdateSlotDTO;
import tn.esprit.etablissement.entity.*;
import tn.esprit.etablissement.enums.DayOfWeek;
import tn.esprit.etablissement.enums.Semester;
import tn.esprit.etablissement.enums.SessionCategory;
import tn.esprit.etablissement.enums.SessionType;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.etablissement.repository.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableRepository timetableRepo;
    private final TimetableSlotRepository slotRepo;
    private final SchoolClassRepository classRepo;
    private final RoomRepository roomRepo;
    private final TeacherCourseRepository teacherCourseRepo;
    private final EstablishmentTimeSlotRepository timeSlotRepo;
    private final PublicHolidayService holidayService;

    private static final String COUNTRY_CODE = "TN";

    private static final DayOfWeek[] ALL_DAYS = {
            DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY
    };
    private DayOfWeek toDayOfWeek(LocalDate date) {
        return switch (date.getDayOfWeek()) {
            case MONDAY -> DayOfWeek.MONDAY;
            case TUESDAY -> DayOfWeek.TUESDAY;
            case WEDNESDAY -> DayOfWeek.WEDNESDAY;
            case THURSDAY -> DayOfWeek.THURSDAY;
            case FRIDAY -> DayOfWeek.FRIDAY;
            default -> DayOfWeek.MONDAY;
        };
    }

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public TimetableSlot addRattrapage(RattrapageDTO dto) {
        SchoolClass sc = classRepo.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Timetable timetable = timetableRepo
                .findBySchoolClassIdAndSemesterAndAcademicYear(
                        sc.getId(),
                        Semester.valueOf(dto.getSemester()),
                        dto.getAcademicYear())
                .orElseGet(() -> {
                    Timetable t = new Timetable();
                    t.setSchoolClass(sc);
                    t.setSemester(Semester.valueOf(dto.getSemester()));
                    t.setAcademicYear(dto.getAcademicYear());
                    return timetableRepo.save(t);
                });

        Room room = roomRepo.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        DayOfWeek day = switch (dto.getDate().getDayOfWeek()) {
            case MONDAY -> DayOfWeek.MONDAY;
            case TUESDAY -> DayOfWeek.TUESDAY;
            case WEDNESDAY -> DayOfWeek.WEDNESDAY;
            case THURSDAY -> DayOfWeek.THURSDAY;
            case FRIDAY -> DayOfWeek.FRIDAY;
            default -> throw new RuntimeException("Invalid day");
        };

        TimetableSlot slot = new TimetableSlot();
        slot.setTimetable(timetable);
        slot.setCourseId(dto.getCourseId());
        slot.setTeacherId(dto.getTeacherId());
        slot.setRoom(room);
        slot.setDayOfWeek(day);
        slot.setSlotDate(dto.getDate());
        slot.setStartTime(dto.getStartTime());
        slot.setEndTime(dto.getEndTime());
        slot.setSessionType(dto.getSessionType());
        slot.setSessionCategory(SessionCategory.RATTRAPAGE);

        return slotRepo.save(slot);
    }
    @Transactional
    public TimetableGenerationResult generateTimetable(
            Long establishmentId, String level,
            Semester semester, String academicYear,
            List<Long> courseIds, LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(4); // Friday
        TimetableGenerationResult result = new TimetableGenerationResult();
        String countryCode = "TN";

        // Build real working days for this specific week
        List<LocalDate> workingDays = new ArrayList<>();
        List<String> skippedHolidays = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            LocalDate day = weekStart.plusDays(i);
            // Skip weekends just in case weekStart is not Monday
            if (day.getDayOfWeek() == java.time.DayOfWeek.SATURDAY
                    || day.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) continue;

            if (holidayService.isHoliday(day, countryCode)) {
                String name = holidayService.getHolidayName(day, countryCode);
                skippedHolidays.add(day + " — " + (name != null ? name : "Public holiday"));
            } else {
                workingDays.add(day);
            }
            result.getWeekDates().add(day.toString());
        }

        result.getSkippedHolidays().addAll(skippedHolidays);

        // Check classes
        List<SchoolClass> classes = classRepo
                .findByDepartmentEstablishmentIdAndLevelAndActiveTrue(
                        establishmentId, level);

        if (classes.isEmpty()) {
            TimetableGenerationResult.UnscheduledCourseInfo info =
                    new TimetableGenerationResult.UnscheduledCourseInfo();
            info.setCourseId(null);
            info.setReason("NO_CLASSES_FOUND");
            info.setDetail("No active classes found for level '" + level + "'.");
            result.getUnscheduledCourses().add(info);
            return result;
        }

        // Check rooms
        List<Room> availableRooms = roomRepo
                .findByEstablishmentIdAndIsAvailableTrue(establishmentId);

        if (availableRooms.isEmpty()) {
            for (Long courseId : courseIds) {
                TimetableGenerationResult.UnscheduledCourseInfo info =
                        new TimetableGenerationResult.UnscheduledCourseInfo();
                info.setCourseId(courseId);
                info.setReason("NO_ROOMS_AVAILABLE");
                info.setDetail("No available rooms in this establishment.");
                result.getUnscheduledCourses().add(info);
            }
            return result;
        }

        // Load time slots
        List<EstablishmentTimeSlot> timeSlots = timeSlotRepo
                .findByEstablishmentIdOrderBySlotOrder(establishmentId);
        if (timeSlots.isEmpty()) {
            timeSlots = List.of(
                    createDefaultSlot(1, 8, 0, 10, 0),
                    createDefaultSlot(2, 10, 0, 12, 0)
            );
        }

        if (workingDays.isEmpty()) {
            for (Long courseId : courseIds) {
                TimetableGenerationResult.UnscheduledCourseInfo info =
                        new TimetableGenerationResult.UnscheduledCourseInfo();
                info.setCourseId(courseId);
                info.setReason("NO_AVAILABLE_DAYS");
                info.setDetail("All days this week are public holidays. " +
                        "Choose a different week.");
                result.getUnscheduledCourses().add(info);
            }
            return result;
        }

        result.setClassesProcessed(classes.size());

        for (SchoolClass sc : classes) {

            Timetable timetable = timetableRepo
                    .findBySchoolClassIdAndSemesterAndAcademicYear(
                            sc.getId(), semester, academicYear)
                    .orElseGet(() -> {
                        Timetable t = new Timetable();
                        t.setSchoolClass(sc);
                        t.setSemester(semester);
                        t.setAcademicYear(academicYear);
                        return timetableRepo.save(t);
                    });

            List<Long> sortedCourses = sortByFewestTeachers(courseIds);
            int dayIndex = 0;
            int slotIndex = 0;

            for (Long courseId : sortedCourses) {
                boolean scheduled = false;

                while (dayIndex < workingDays.size()) {
                    LocalDate realDate = workingDays.get(dayIndex);
                    DayOfWeek day = toDayOfWeek(realDate);
                    LocalTime start = timeSlots.get(slotIndex).getStartTime();
                    LocalTime end = timeSlots.get(slotIndex).getEndTime();

                    Long teacherId = findAvailableTeacherSimple(courseId, day, start, end, weekStart, weekEnd);                    Room room = findAvailableRoom(
                            availableRooms, sc.getMaxStudents(), day, start, end, weekStart, weekEnd);

                    if (teacherId != null && room != null) {
                        TimetableSlot slot = new TimetableSlot();
                        slot.setTimetable(timetable);
                        slot.setCourseId(courseId);
                        slot.setTeacherId(teacherId);
                        slot.setRoom(room);
                        slot.setDayOfWeek(day);
                        slot.setSlotDate(realDate);
                        slot.setStartTime(start);
                        slot.setEndTime(end);
                        slot.setSessionType(SessionType.LECTURE);
                        slotRepo.save(slot);
                        entityManager.flush();
                        result.setScheduledCourses(result.getScheduledCourses() + 1);

                        TimetableGenerationResult.ScheduledSlotInfo info =
                                new TimetableGenerationResult.ScheduledSlotInfo();
                        info.setCourseId(courseId);
                        info.setTeacherId(teacherId);
                        info.setRoomName(room.getName());
                        info.setDayOfWeek(day.name());
                        info.setSlotDate(realDate.toString());
                        info.setStartTime(start.toString());
                        info.setEndTime(end.toString());
                        info.setClassName(sc.getName());
                        result.getScheduledSlots().add(info);

                        slotIndex++;
                        if (slotIndex >= timeSlots.size()) {
                            slotIndex = 0;
                            dayIndex++;
                        }
                        scheduled = true;
                        break;

                    } else {
                        slotIndex++;
                        if (slotIndex >= timeSlots.size()) {
                            slotIndex = 0;
                            dayIndex++;
                        }
                    }
                }

                if (!scheduled) {
                    TimetableGenerationResult.UnscheduledCourseInfo info =
                            new TimetableGenerationResult.UnscheduledCourseInfo();
                    info.setCourseId(courseId);
                    String reason = diagnoseFailure(
                            courseId, sc.getMaxStudents(),
                            availableRooms, academicYear,
                            workingDays, weekStart);
                    info.setReason(reason.split("\\|")[0]);
                    info.setDetail(reason.split("\\|")[1]);
                    result.getUnscheduledCourses().add(info);
                }
            }
        }

        return result;
    }

    // --- Build working days, skipping public holidays ---
    private List<DayOfWeek> buildAvailableDays(int year) {
        List<DayOfWeek> availableDays = new ArrayList<>();
        Set<LocalDate> holidays = holidayService.getHolidays(year, COUNTRY_CODE);

        // Find the first full Mon-Fri week of September
        LocalDate weekStart = LocalDate.of(year, 9, 1);
        while (weekStart.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            weekStart = weekStart.plusDays(1);
        }

        for (int i = 0; i < 5; i++) {
            LocalDate day = weekStart.plusDays(i);
            DayOfWeek mapped = holidayService.toDayOfWeek(day);
            if (mapped != null) {
                if (holidays.contains(day)) {
                    String holidayName = holidayService.getHolidayName(day, COUNTRY_CODE);
                    String label = day + " (" + (holidayName != null ? holidayName : "Public holiday") + ")";
                    // Don't add to available days — it's a holiday
                    // We still want to record it was skipped
                } else {
                    availableDays.add(mapped);
                }
            }
        }

        // If fewer than 5 days available, check next week too
        if (availableDays.size() < 5) {
            LocalDate nextWeek = weekStart.plusWeeks(1);
            for (int i = 0; i < 5 && availableDays.size() < 5; i++) {
                LocalDate day = nextWeek.plusDays(i);
                DayOfWeek mapped = holidayService.toDayOfWeek(day);
                if (mapped != null && !holidays.contains(day)
                        && !availableDays.contains(mapped)) {
                    availableDays.add(mapped);
                }
            }
        }

        return availableDays;
    }

    // --- Build skipped holidays list for result ---
    public List<String> getSkippedHolidays(int year) {
        List<String> skipped = new ArrayList<>();
        Set<LocalDate> holidays = holidayService.getHolidays(year, COUNTRY_CODE);

        LocalDate weekStart = LocalDate.of(year, 9, 1);
        while (weekStart.getDayOfWeek() != java.time.DayOfWeek.MONDAY) {
            weekStart = weekStart.plusDays(1);
        }

        for (int i = 0; i < 5; i++) {
            LocalDate day = weekStart.plusDays(i);
            if (holidays.contains(day)) {
                String name = holidayService.getHolidayName(day, COUNTRY_CODE);
                skipped.add(day + " — " + (name != null ? name : "Public holiday"));
            }
        }
        return skipped;
    }

    private String diagnoseFailure(Long courseId, int classSize,
                                   List<Room> availableRooms,
                                   String academicYear,
                                   List<LocalDate> workingDays,
                                   LocalDate weekStart) {

        List<TeacherCourse> eligible = teacherCourseRepo.findByCourseId(courseId);

        if (eligible.isEmpty()) {
            return "NO_TEACHER_ASSIGNED|No teacher is assigned to course " +
                    courseId + ". Assign at least one teacher before generating.";
        }

        LocalDate weekEnd = workingDays.isEmpty()
                ? weekStart.plusDays(4)
                : workingDays.get(workingDays.size() - 1);

        boolean allFull = eligible.stream().allMatch(tc -> {
            long count = slotRepo.countByTeacherIdAndWeek(
                    tc.getTeacherId(), weekStart, weekEnd);
            return count >= 5;
        });

        if (allFull) {
            return "TEACHER_OVERLOADED|All teachers for course " + courseId +
                    " already have 5 courses this week.";
        }


        boolean noRoomAvailable = availableRooms.stream()
                .filter(r -> r.getCapacity() >= classSize)
                .allMatch(r -> {
                    for (LocalDate d : workingDays) {
                        DayOfWeek dow = toDayOfWeek(d);
                        // If any working day has a free room, it's not a room problem
                        long conflicts = slotRepo.countRoomConflictsInWeek(
                                r.getId(), weekStart, weekEnd, dow,
                                LocalTime.of(8, 0), LocalTime.of(20, 0));
                        if (conflicts == 0) return false;
                    }
                    return true;
                });

        if (availableRooms.stream().noneMatch(r -> r.getCapacity() >= classSize)) {
            return "NO_SUITABLE_ROOM|No room has enough capacity for " +
                    classSize + " students.";
        }

        if (noRoomAvailable) {
            return "NO_SUITABLE_ROOM|All rooms are fully booked this week. " +
                    "Try a different week or add more rooms.";
        }

        if (workingDays.isEmpty()) {
            return "NO_AVAILABLE_DAYS|All days this week are public holidays.";
        }

        return "NO_FREE_SLOT|All slots for the week are occupied for course " +
                courseId + ". Add more time slots in schedule configuration.";
    }

    private List<Long> sortByFewestTeachers(List<Long> courseIds) {
        List<Long> sorted = new ArrayList<>(courseIds);
        sorted.sort(Comparator.comparingInt(
                id -> teacherCourseRepo.findByCourseId(id).size()
        ));
        return sorted;
    }

    private Long findAvailableTeacherSimple(Long courseId, DayOfWeek day,
                                            LocalTime start, LocalTime end,
                                            LocalDate weekStart, LocalDate weekEnd) {
        List<TeacherCourse> eligible = teacherCourseRepo.findByCourseId(courseId);
        for (TeacherCourse tc : eligible) {
            Long tid = tc.getTeacherId();

            boolean doubleBooked = slotRepo
                    .existsByTeacherIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                            tid, day, end, start);

            // Count only slots in THIS specific week
            long weeklyCount = slotRepo.countByTeacherIdAndWeek(
                    tid, weekStart, weekEnd);

            if (!doubleBooked && weeklyCount < 5) return tid;
        }
        return null;
    }

    private Room findAvailableRoom(List<Room> rooms, int minCapacity,
                                   DayOfWeek day, LocalTime start, LocalTime end,
                                   LocalDate weekStart, LocalDate weekEnd) {
        for (Room room : rooms) {
            if (room.getCapacity() < minCapacity) continue;

            // Week-specific conflict check
            long conflicts = slotRepo.countRoomConflictsInWeek(
                    room.getId(), weekStart, weekEnd, day, start, end);

            if (conflicts == 0) return room;
        }
        return null;
    }

    public List<TimetableSlot> getSlotsByClass(Long classId) {
        List<Timetable> timetables = timetableRepo.findBySchoolClassId(classId);
        List<TimetableSlot> allSlots = new ArrayList<>();
        for (Timetable t : timetables) {
            allSlots.addAll(slotRepo.findByTimetableId(t.getId()));
        }
        return allSlots;
    }

    public List<TimetableSlot> getSlotsByTeacher(Long teacherId) {
        return slotRepo.findByTeacherId(teacherId);
    }

    public void deleteSlot(Long slotId) {
        slotRepo.deleteById(slotId);
    }

    public TimetableSlot updateSlot(Long slotId, UpdateSlotDTO dto) {
        TimetableSlot existing = slotRepo.findById(slotId)
                .orElseThrow(() -> new RuntimeException("Slot not found: " + slotId));

        Room room = roomRepo.findById(dto.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        boolean roomConflict = slotRepo
                .existsByRoomIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                        room.getId(), dto.getDayOfWeek(),
                        dto.getEndTime(), dto.getStartTime());

        boolean teacherConflict = slotRepo
                .existsByTeacherIdAndDayOfWeekAndStartTimeLessThanAndEndTimeGreaterThan(
                        dto.getTeacherId(), dto.getDayOfWeek(),
                        dto.getEndTime(), dto.getStartTime());

        if (roomConflict && !room.getId().equals(existing.getRoom().getId())) {
            throw new RuntimeException(
                    "ROOM_CONFLICT|Room " + room.getName() +
                            " is already booked on " + dto.getDayOfWeek() +
                            " at this time. Choose a different room or time.");
        }

        if (teacherConflict && !dto.getTeacherId().equals(existing.getTeacherId())) {
            throw new RuntimeException(
                    "TEACHER_CONFLICT|Teacher " + dto.getTeacherId() +
                            " is already booked on " + dto.getDayOfWeek() +
                            " at this time. Choose a different teacher or time.");
        }

        existing.setDayOfWeek(dto.getDayOfWeek());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        existing.setRoom(room);
        existing.setTeacherId(dto.getTeacherId());
        existing.setSessionType(dto.getSessionType());
        return slotRepo.save(existing);
    }

    private EstablishmentTimeSlot createDefaultSlot(
            int order, int startH, int startM, int endH, int endM) {
        EstablishmentTimeSlot s = new EstablishmentTimeSlot();
        s.setSlotOrder(order);
        s.setStartTime(LocalTime.of(startH, startM));
        s.setEndTime(LocalTime.of(endH, endM));
        s.setLabel("Slot " + order);
        return s;
    }
    @Transactional
    public TimetableGenerationResult copyWeek(
            Long establishmentId, LocalDate sourceWeek,
            LocalDate targetWeek, String academicYear, Semester semester) {

        TimetableGenerationResult result = new TimetableGenerationResult();
        String countryCode = "TN";

        // Load all slots from source week
        LocalDate sourceEnd = sourceWeek.plusDays(4);
        List<TimetableSlot> sourceSlots = slotRepo
                .findBySlotDateBetweenAndTimetable_SchoolClass_Department_Establishment_Id(
                        sourceWeek, sourceEnd, establishmentId);

        if (sourceSlots.isEmpty()) {
            TimetableGenerationResult.UnscheduledCourseInfo info =
                    new TimetableGenerationResult.UnscheduledCourseInfo();
            info.setReason("NO_SOURCE_SLOTS");
            info.setDetail("No slots found for the source week " +
                    sourceWeek + ". Please select a week that has been generated.");
            result.getUnscheduledCourses().add(info);
            return result;
        }

        // Build target working days — skip holidays
        List<LocalDate> targetWorkingDays = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocalDate day = targetWeek.plusDays(i);
            if (holidayService.isHoliday(day, countryCode)) {
                String name = holidayService.getHolidayName(day, countryCode);
                result.getSkippedHolidays().add(day + " — " +
                        (name != null ? name : "Public holiday"));
            } else {
                targetWorkingDays.add(day);
            }
        }

        // Map source day of week to target date
        Map<DayOfWeek, LocalDate> dayMap = new HashMap<>();
        for (LocalDate d : targetWorkingDays) {
            DayOfWeek dow = toDayOfWeek(d);
            dayMap.put(dow, d);
        }

        int scheduled = 0;
        for (TimetableSlot source : sourceSlots) {
            LocalDate targetDate = dayMap.get(source.getDayOfWeek());
            if (targetDate == null) {
                // This day is a holiday in the target week — skip
                TimetableGenerationResult.UnscheduledCourseInfo info =
                        new TimetableGenerationResult.UnscheduledCourseInfo();
                info.setCourseId(source.getCourseId());
                info.setReason("HOLIDAY_IN_TARGET");
                info.setDetail("Course " + source.getCourseId() +
                        " falls on a holiday in the target week.");
                result.getUnscheduledCourses().add(info);
                continue;
            }

            // Get or create timetable for target week's class
            Timetable timetable = timetableRepo
                    .findBySchoolClassIdAndSemesterAndAcademicYear(
                            source.getTimetable().getSchoolClass().getId(),
                            semester, academicYear)
                    .orElseGet(() -> {
                        Timetable t = new Timetable();
                        t.setSchoolClass(source.getTimetable().getSchoolClass());
                        t.setSemester(semester);
                        t.setAcademicYear(academicYear);
                        return timetableRepo.save(t);
                    });

            TimetableSlot newSlot = new TimetableSlot();
            newSlot.setTimetable(timetable);
            newSlot.setCourseId(source.getCourseId());
            newSlot.setTeacherId(source.getTeacherId());
            newSlot.setRoom(source.getRoom());
            newSlot.setDayOfWeek(source.getDayOfWeek());
            newSlot.setSlotDate(targetDate);
            newSlot.setStartTime(source.getStartTime());
            newSlot.setEndTime(source.getEndTime());
            newSlot.setSessionType(source.getSessionType());
            newSlot.setSessionCategory(
                    source.getSessionCategory() != null
                            ? source.getSessionCategory()
                            : SessionCategory.NORMAL);

            slotRepo.save(newSlot);
            scheduled++;

            TimetableGenerationResult.ScheduledSlotInfo info =
                    new TimetableGenerationResult.ScheduledSlotInfo();
            info.setCourseId(source.getCourseId());
            info.setTeacherId(source.getTeacherId());
            info.setRoomName(source.getRoom().getName());
            info.setDayOfWeek(source.getDayOfWeek().name());
            info.setSlotDate(targetDate.toString());
            info.setStartTime(source.getStartTime().toString());
            info.setEndTime(source.getEndTime().toString());
            info.setClassName(source.getTimetable().getSchoolClass().getName());
            result.getScheduledSlots().add(info);
        }

        result.setScheduledCourses(scheduled);
        result.setClassesProcessed(
                (int) sourceSlots.stream()
                        .map(s -> s.getTimetable().getSchoolClass().getId())
                        .distinct().count());

        return result;
    }
    public List<Map<String, Object>> getTeacherClasses(Long teacherId) {
        List<TimetableSlot> slots = slotRepo.findByTeacherId(teacherId);
        return slots.stream()
                .map(s -> s.getTimetable().getSchoolClass())
                .collect(java.util.stream.Collectors.toMap(
                        sc -> sc.getId(),
                        sc -> sc,
                        (a, b) -> a))
                .values().stream()
                .map(sc -> {
                    Map<String, Object> map = new java.util.HashMap<>();
                    map.put("classId", sc.getId());
                    map.put("className", sc.getName());
                    map.put("level", sc.getLevel());
                    map.put("academicYear", sc.getAcademicYear());
                    map.put("currentStudents", sc.getCurrentStudents());
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    public List<Long> getTeacherCoursesForClass(Long teacherId, Long classId) {
        return slotRepo.findByTeacherId(teacherId).stream()
                .filter(s -> s.getTimetable()
                        .getSchoolClass().getId().equals(classId))
                .map(TimetableSlot::getCourseId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }
    public List<Long> getCoursesByClass(Long classId) {
        List<Timetable> timetables = timetableRepo.findBySchoolClassId(classId);
        return timetables.stream()
                .flatMap(t -> slotRepo.findByTimetableId(t.getId()).stream())
                .map(TimetableSlot::getCourseId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }
    public List<Long> getCoursesByLevel(Long establishmentId, String level) {
        List<SchoolClass> classes = classRepo
                .findByDepartmentEstablishmentIdAndLevelAndActiveTrue(
                        establishmentId, level);
        return classes.stream()
                .flatMap(sc -> {
                    List<Timetable> timetables =
                            timetableRepo.findBySchoolClassId(sc.getId());
                    return timetables.stream()
                            .flatMap(t -> slotRepo.findByTimetableId(t.getId()).stream());
                })
                .map(TimetableSlot::getCourseId)
                .distinct()
                .collect(java.util.stream.Collectors.toList());
    }
}
