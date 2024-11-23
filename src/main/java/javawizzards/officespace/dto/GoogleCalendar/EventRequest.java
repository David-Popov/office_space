package javawizzards.officespace.dto.GoogleCalendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequest {
    private String summary;
    private String description;
    private String startTime;
    private String endTime;
    private String timeZone;
}
