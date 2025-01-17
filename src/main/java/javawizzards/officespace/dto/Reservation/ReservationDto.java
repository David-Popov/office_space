package javawizzards.officespace.dto.Reservation;

import jakarta.validation.Valid;
import javawizzards.officespace.dto.User.UserDto;
import javawizzards.officespace.enumerations.Reservation.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReservationDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("reservation_title")
    private String reservationTitle;

    @NotNull(message = "Reservation user id can't be null")
    @JsonProperty("userUuid")
    private UUID userId;

    @NotNull(message = "Reservation startDateTime can't be null")
    @JsonProperty("startDateTime")
    private LocalDateTime startDateTime;

    @NotNull(message = "Reservation endDateTime can't be null")
    @JsonProperty("endDateTime")
    private LocalDateTime endDateTime;

    @NotNull(message = "Duration can't be null")
    @JsonProperty("durationAsHours")
    private int durationAsHours;

    @NotNull(message = "Reservation status can't be null")
    @JsonProperty("status")
    private ReservationStatus status;

    @NotNull(message = "Reservation officeRoomId can't be null")
    @JsonProperty("officeRoomUuid")
    private UUID officeRoomId;

    @Valid
    private EventDto event;
}
