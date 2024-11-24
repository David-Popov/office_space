package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByOfficeRoomId(UUID officeRoomId);
    List<Reservation> findByUserId(UUID userId);
    Optional<Reservation> findByOfficeRoomIdAndStartDateTimeBetween(UUID officeRoomId, LocalDateTime startDateTime, LocalDateTime endDateTime);
}