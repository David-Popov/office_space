
package javawizzards.officespace.service.GoogleCalendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.EventDateTime;
import javawizzards.officespace.Auth.GoogleCalendarAuth;
import javawizzards.officespace.configuration.GoogleCalendarLibConfig;
import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.repository.OfficeRoomRepository;
import javawizzards.officespace.utility.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleCalendarServiceImpl implements GoogleCalendarService {

    private final GoogleCalendarLibConfig config;
    private final GoogleCalendarAuth authentication;
    private final OfficeRoomRepository officeRoomRepository;

    @Override
    @SneakyThrows
    public void createEvent(CreateReservationDto dto) {
        validateReservationDto(dto); // Validate the CreateReservationDto

        // Check if an event already exists in Google Calendar
        //TODO

        Event event = createCalendarModelEvent(dto);
        sendEventToCalendarAPI(dto, event);
    }

    private boolean checkEventAlreadyExists(CreateReservationDto dto) {
        List<Event> events = getEvents(dto.getStartDateTime(), dto.getEndDateTime());
        return !events.isEmpty();
    }

    private Event createCalendarModelEvent(CreateReservationDto dto) {
        if (dto == null || dto.getEvent() == null) {
            throw new IllegalArgumentException("Reservation and event details are required.");
        }

        // Извличане на информация за офиса от репозиторито
        String location = "No location provided";
        if (dto.getOfficeRoomId() != null) {
            var officeRoom = officeRoomRepository.findById(dto.getOfficeRoomId());
            if (officeRoom.isPresent() && officeRoom.get().getCompany() != null) {
                location = officeRoom.get().getCompany().getAddress();
            }
        }

        Event event = new Event()
                .setSummary(dto.getEvent().getMeetingTitle())
                .setLocation(location)
                .setDescription(dto.getEvent().getDescription());

        EventDateTime start = new EventDateTime()
                .setDateTime(DateUtils.convertLocalDateTimeToDateTime(dto.getStartDateTime()))
                .setTimeZone("America/Los_Angeles");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(DateUtils.convertLocalDateTimeToDateTime(dto.getEndDateTime()))
                .setTimeZone("America/Los_Angeles");
        event.setEnd(end);

        Event.Reminders reminders = new Event.Reminders().setUseDefault(false);
        event.setReminders(reminders);

        return event;
    }

    @SneakyThrows
    private void sendEventToCalendarAPI(CreateReservationDto dto, Event event) {
        Calendar service = authentication.getService();

        if (config.isLogEnabled()) {
            log.info("Sending Event to Google Calendar...");
            log.info("Meeting Title: {}, Dates: {} - {}", dto.getEvent().getMeetingTitle(), dto.getStartDateTime(), dto.getEndDateTime());
        }

        String calendarId = "primary";
        service.events()
                .insert(calendarId, event)
                .setSendUpdates("all")
                .execute();
    }

    @Override
    @SneakyThrows
    public List<Event> getEvents(LocalDateTime dateTimeStart, LocalDateTime dateTimeEnd) {
        Calendar calendarService = authentication.getService();

        DateTime timeMin = DateUtils.convertLocalDateTimeToDateTime(dateTimeStart);
        DateTime timeMax = DateUtils.convertLocalDateTimeToDateTime(dateTimeEnd);

        Events events = calendarService
                .events()
                .list("primary")
                .setTimeMin(timeMin)
                .setTimeMax(timeMax)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        return events.getItems();
    }

    private void validateReservationDto(CreateReservationDto dto) {
        if (dto.getEvent() == null) {
            throw new IllegalArgumentException("Event details are required.");
        }
        if (dto.getStartDateTime() == null || dto.getEndDateTime() == null) {
            throw new IllegalArgumentException("Start and end times are required.");
        }
        if (dto.getEvent().getMeetingTitle() == null || dto.getEvent().getMeetingTitle().isEmpty()) {
            throw new IllegalArgumentException("Meeting title is required.");
        }
    }
}
