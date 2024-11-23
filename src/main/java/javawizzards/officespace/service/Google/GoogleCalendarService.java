package javawizzards.officespace.service.Google;

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
import com.google.api.services.calendar.model.Events;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class GoogleCalendarService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    @Value("${google.calendar.application-name}")
    private String applicationName;

    @Value("${google.calendar.credentials-file-path}")
    private String credentialsFilePath;

    private Calendar calendarService;

    @PostConstruct
    public void init() throws GeneralSecurityException, IOException {
        log.info("Initializing Google Calendar service for application: {}", applicationName);
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        calendarService = new Calendar.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(applicationName)
                .build();
        log.info("Google Calendar service initialized successfully");
    }

    private Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        try {
            // Load client secrets from file
            InputStream in = GoogleCalendarService.class.getResourceAsStream(credentialsFilePath);
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + credentialsFilePath);
            }

            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            File tokensDirectory = new File(TOKENS_DIRECTORY_PATH);
            if (!tokensDirectory.exists()) {
                tokensDirectory.mkdirs();
            }

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(tokensDirectory))
                    .setAccessType("offline")
                    .build();

            // Modified LocalServerReceiver configuration
            LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                    .setPort(8888)
                    .setHost("localhost")
                    .setCallbackPath("/Callback")
                    .build();

            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
        } catch (Exception e) {
            log.error("Failed to get credentials", e);
            throw e;
        }
    }

    /**
     * Lists the next events from the primary calendar
     */
    public List<Event> getUpcomingEvents(int maxResults) throws IOException {
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = calendarService.events().list("primary")
                .setMaxResults(maxResults)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        return events.getItems();
    }

    /**
     * Creates a new calendar event
     */
    public Event createEvent(Event event) throws IOException {
        return calendarService.events().insert("primary", event).execute();
    }

    /**
     * Gets an event by ID
     */
    public Event getEvent(String eventId) throws IOException {
        return calendarService.events().get("primary", eventId).execute();
    }

    /**
     * Updates an existing event
     */
    public Event updateEvent(String eventId, Event event) throws IOException {
        return calendarService.events().update("primary", eventId, event).execute();
    }

    /**
     * Deletes an event
     */
    public void deleteEvent(String eventId) throws IOException {
        calendarService.events().delete("primary", eventId).execute();
    }
}