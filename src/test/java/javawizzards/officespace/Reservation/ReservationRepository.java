package javawizzards.officespace.Reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("reservationReservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    // Your methods
}

