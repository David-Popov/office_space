package javawizzards.officespace.Repo;

import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.entity.Reservation;
import javawizzards.officespace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository("repoReservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findByOfficeRoom(OfficeRoom officeRoom);
    List<Reservation> findByUser(User user);
}


