package javawizzards.officespace.service.Ticket;

import java.util.List;
import java.util.UUID;

import javawizzards.officespace.dto.Ticket.TicketDto;
import javawizzards.officespace.entity.Ticket;
import javawizzards.officespace.enumerations.Ticket.*;

public interface TicketService {
    TicketDto createTicket(TicketDto ticketDto);
    Ticket changeTicketType(UUID id, TicketType newType);
    void deleteTicket(UUID id);
    Ticket changeTicketStatus(UUID id, TicketStatus newStatus);
    List<TicketDto> getAllTicketsOfUser(UUID userId);
    UUID findDepartmentByTicketType(TicketType type);
}