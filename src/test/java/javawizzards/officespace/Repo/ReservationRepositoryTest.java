package javawizzards.officespace.Repo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javawizzards.officespace.entity.Reservation;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.enumerations.Reservation.ReservationStatus;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    void testSaveAndFindById() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("JohnDoe");

        OfficeRoom officeRoom = new OfficeRoom();
        officeRoom.setId(UUID.randomUUID());
        officeRoom.setName("Room A"); // Updated field

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setOfficeRoom(officeRoom);
        reservation.setStartDateTime(LocalDateTime.now());
        reservation.setEndDateTime(LocalDateTime.now().plusHours(2));
        reservation.setDurationAsHours(2);
        reservation.setStatus(ReservationStatus.CONFIRMED);

        // Act
        Reservation savedReservation = reservationRepository.save(reservation);
        Optional<Reservation> foundReservation = reservationRepository.findById(savedReservation.getId());

        // Assert
        assertTrue(foundReservation.isPresent());
        assertEquals(savedReservation.getId(), foundReservation.get().getId());
    }

    @Test
    void testFindByOfficeRoom() {
        // Arrange
        OfficeRoom officeRoom = new OfficeRoom();
        officeRoom.setId(UUID.randomUUID());
        officeRoom.setName("Room B"); // Updated field

        Reservation reservation1 = new Reservation();
        reservation1.setOfficeRoom(officeRoom);
        reservation1.setStartDateTime(LocalDateTime.now());
        reservation1.setEndDateTime(LocalDateTime.now().plusHours(1));
        reservation1.setDurationAsHours(1);
        reservation1.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation1);

        Reservation reservation2 = new Reservation();
        reservation2.setOfficeRoom(officeRoom);
        reservation2.setStartDateTime(LocalDateTime.now().plusHours(2));
        reservation2.setEndDateTime(LocalDateTime.now().plusHours(3));
        reservation2.setDurationAsHours(1);
        reservation2.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation2);

        // Act
        List<Reservation> reservations = reservationRepository.findByOfficeRoom(officeRoom);

        // Assert
        assertEquals(2, reservations.size());
    }

    @Test
    void testFindByUser() {
        // Arrange
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("JaneDoe");

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setStartDateTime(LocalDateTime.now());
        reservation.setEndDateTime(LocalDateTime.now().plusHours(1));
        reservation.setDurationAsHours(1);
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        // Act
        List<Reservation> reservations = reservationRepository.findByUser(user);

        // Assert
        assertEquals(1, reservations.size());
        assertEquals(user.getId(), reservations.get(0).getUser().getId());
    }
}
