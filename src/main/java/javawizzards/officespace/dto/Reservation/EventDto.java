package javawizzards.officespace.dto.Reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private UUID id;

    @NotNull
    private String meetingTitle;

    private String description;

    private List<String> attendees;

    @NotNull
    @Email
    private String contactEmail;

    @NotNull
    private String department;

    private UUID reservationId;
}
