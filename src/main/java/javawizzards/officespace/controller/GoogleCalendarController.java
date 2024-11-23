package javawizzards.officespace.controller;


import com.google.api.services.calendar.model.Event;
import javawizzards.officespace.service.Google.GoogleCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
public class GoogleCalendarController {
    private final GoogleCalendarService calendarService;

    public GoogleCalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getUpcomingEvents(
            @RequestParam(defaultValue = "10") int maxResults) throws IOException {
        return ResponseEntity.ok(calendarService.getUpcomingEvents(maxResults));
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) throws IOException {
        return ResponseEntity.ok(calendarService.createEvent(event));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<Event> getEvent(@PathVariable String eventId) throws IOException {
        return ResponseEntity.ok(calendarService.getEvent(eventId));
    }

    @PutMapping("/events/{eventId}")
    public ResponseEntity<Event> updateEvent(
            @PathVariable String eventId,
            @RequestBody Event event) throws IOException {
        return ResponseEntity.ok(calendarService.updateEvent(eventId, event));
    }

    @DeleteMapping("/events/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String eventId) throws IOException {
        calendarService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}
