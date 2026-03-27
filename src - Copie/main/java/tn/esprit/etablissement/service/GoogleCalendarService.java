package tn.esprit.etablissement.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.etablissement.entity.TimetableSlot;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "LMS Etablissement";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        InputStream in = GoogleCalendarService.class
                .getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException(
                    "Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(
                        new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver)
                .authorize("user");
    }

    private Calendar getCalendarService() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT =
                GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<String> exportSlotsToCalendar(
            List<TimetableSlot> slots, String calendarId) throws Exception {

        Calendar service = getCalendarService();
        List<String> createdEventIds = new ArrayList<>();

        System.out.println("Total slots received: " + slots.size());

        for (TimetableSlot slot : slots) {
            if (slot.getSlotDate() == null) {
                System.out.println("Skipping slot " + slot.getId() + " — no date");
                continue;
            }

            System.out.println("Exporting slot: course=" + slot.getCourseId()
                    + " date=" + slot.getSlotDate()
                    + " time=" + slot.getStartTime() + "-" + slot.getEndTime());

            Event event = buildEvent(slot);
            Event created = service.events()
                    .insert(calendarId, event).execute();
            createdEventIds.add(created.getId());
            System.out.println("Created event: " + created.getId());
        }

        System.out.println("Total events created: " + createdEventIds.size());
        return createdEventIds;
    }

    private Event buildEvent(TimetableSlot slot) {
        Event event = new Event();

        String summary = "Course " + slot.getCourseId()
                + " — " + slot.getSessionType().name();
        event.setSummary(summary);

        String description = "Teacher ID: " + slot.getTeacherId()
                + "\nRoom: " + (slot.getRoom() != null ? slot.getRoom().getName() : "TBD")
                + "\nSession type: " + slot.getSessionType().name();
        event.setDescription(description);

        // Start datetime
        LocalDateTime startDt = LocalDateTime.of(
                slot.getSlotDate(), slot.getStartTime());
        DateTime start = new DateTime(
                startDt.atZone(ZoneId.of("Africa/Tunis"))
                        .toInstant().toEpochMilli());
        event.setStart(new EventDateTime()
                .setDateTime(start)
                .setTimeZone("Africa/Tunis"));

        // End datetime
        LocalDateTime endDt = LocalDateTime.of(
                slot.getSlotDate(), slot.getEndTime());
        DateTime end = new DateTime(
                endDt.atZone(ZoneId.of("Africa/Tunis"))
                        .toInstant().toEpochMilli());
        event.setEnd(new EventDateTime()
                .setDateTime(end)
                .setTimeZone("Africa/Tunis"));

        return event;
    }

    public String getAuthUrl() throws Exception {
        final NetHttpTransport HTTP_TRANSPORT =
                GoogleNetHttpTransport.newTrustedTransport();
        InputStream in = GoogleCalendarService.class
                .getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(
                        new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        return flow.newAuthorizationUrl()
                .setRedirectUri("http://localhost:8888/Callback")
                .build();
    }
}