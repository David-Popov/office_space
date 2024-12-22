package javawizzards.officespace.dto.Ticket;

import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.enumerations.Ticket.TicketStatus;
import javawizzards.officespace.enumerations.Ticket.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TicketDto {
    @NotNull(message = "UserId can't be null")
    @JsonProperty("user_uuid")
    private UUID userId;

    @NotNull(message = "TicketTitle can't be null")
    @JsonProperty("ticketTitle")
    private String ticketTitle;

    @NotNull(message = "TicketDesc can't be null")
    @JsonProperty("ticketDesc")
    private String ticketDesc;

    @NotNull(message = "TicketType can't be null")
    @JsonProperty("ticketType")
    private TicketType ticketType;

    @NotNull(message = "TicketStatus can't be null")
    @JsonProperty("ticketStatus")
    private TicketStatus ticketStatus;

    @NotNull(message = "OfficeRoomId can't be null")
    @JsonProperty("officeRoom_uuid")
    private UUID officeRoomId;

    @NotNull(message = "TicketDate can't be null")
    @JsonProperty("ticketDate")
    private LocalDateTime ticketDate;
}
