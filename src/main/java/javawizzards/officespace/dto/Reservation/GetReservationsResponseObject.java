package javawizzards.officespace.dto.Reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.Reservation.ReservationStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class GetReservationsResponseObject {
    @JsonProperty("id")
    private UUID id;

    @JsonProperty("reservationTitle")
    private String reservationTitle;

    @NotNull(message = "User email can't be null")
    @JsonProperty("userEmail")
    private String userEmail;

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

    @NotNull(message = "Office Room name can't be null")
    @JsonProperty("officeRoomName")
    private String officeRoomName;

    @Valid
    private EventDto event;
}
