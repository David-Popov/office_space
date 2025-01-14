package javawizzards.officespace.Auth;

import javawizzards.officespace.configuration.GoogleCalendarLibConfig;
import javawizzards.officespace.exception.GoogleCalendar.GoogleCalendarLibException;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GoogleCalendarAuth {

    private final GoogleCalendarLibConfig config;

    //public static final String APPLICATION_NAME = "Office Space Reservation";
    @Value("${google.calendar.application-name}")
    private String googleCalendarApplicationName;

    // If modifying these scopes, delete your previously saved tokens folder
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    //private static final String TOKENS_DIRECTORY_PATH = "tokens";
    @Value("${google.calendar.tokens-directory-path}")
    private String tokensDirectoryPath;

    @Value("${google.calendar.credentials-directory-path}")
    private String credentialsDirectoryPath;

    @Value("${google.calendar.client-id}")
    private String clientId;

    @Value("${google.calendar.client-secret}")
    private String clientSecret;

    @Value("${google.calendar.redirect-uri}")
    private String redirectUri;

    @Bean
    @SneakyThrows
    public Calendar getService() {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            return new Calendar.Builder(
                    httpTransport,
                    JSON_FACTORY,
                    getCredentials(httpTransport, config.getCredentialsPath()))
                    .setApplicationName(googleCalendarApplicationName)
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            throw new GoogleCalendarLibException("Error to get Google Calendar Service", e);
        }
    }

    @SneakyThrows
    private Credential getCredentials(
            final NetHttpTransport httpTransport, final String pathCredentials) {

        GoogleClientSecrets clientSecrets = getClientSecrets(pathCredentials);

        try {
            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow =
                    new GoogleAuthorizationCodeFlow.Builder(
                            httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                            .setDataStoreFactory(
                                    new FileDataStoreFactory(new File(tokensDirectoryPath)))
                            .build();

            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        } catch (Exception e) {
            throw new GoogleCalendarLibException("Error get Google Api Client OAuth Credential", e);
        }
    }

    @SneakyThrows
    private GoogleClientSecrets getClientSecrets(final String pathCredentials) {
        try (InputStream inputStream = getInputStream(pathCredentials)) {
            return loadGoogleClientSecrets(inputStream);
        }
    }

    @SneakyThrows
    private InputStream getInputStream(String pathCredentials) {
        try {
            Path path = Paths.get(credentialsDirectoryPath, "google-credentials.json");
            return Files.newInputStream(Paths.get(path.toString()));

        } catch (IOException e) {
            throw new GoogleCalendarLibException("Resource not found: google-credentials.json", e);
        }
    }

    @SneakyThrows
    private GoogleClientSecrets loadGoogleClientSecrets(InputStream inputStream) {
        try {
            return GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));
        } catch (IOException e) {
            throw new GoogleCalendarLibException("Error to get GoogleClientSecrets", e);
        }
    }
}