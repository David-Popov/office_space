package javawizzards.officespace.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import javawizzards.officespace.entity.Ticket;

import java.util.List;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    List<Ticket> findAllByUserId(UUID userId);
    
}