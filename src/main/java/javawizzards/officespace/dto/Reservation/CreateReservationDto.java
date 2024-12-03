package javawizzards.officespace.dto.Reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateReservationDto {
    @JsonProperty("reservation_title")
    private String reservationTitle;

    @NotNull(message = "User ID can't be null")
    @JsonProperty("user_uuid")
    private UUID userId;

    @NotNull(message = "Reservation startDateTime can't be null")
    @JsonProperty("start_date_time")
    private LocalDateTime startDateTime;

    @NotNull(message = "Reservation endDateTime can't be null")
    @JsonProperty("end_date_time")
    private LocalDateTime endDateTime;

    @NotNull(message = "Duration can't be null")
    @JsonProperty("durationAsHours")
    private int durationAsHours;

    @NotNull(message = "Reservation officeRoomId can't be null")
    @JsonProperty("office_room_uuid")
    private UUID officeRoomId;

    @JsonProperty("participant_uuids")
    private List<UUID> participantIds;

    @Valid
    private EventDto event;
}
