package javawizzards.officespace.Reservation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReservationSuccess() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setDetails("Test Details");
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        // Act
        Reservation createdReservation = reservationService.createReservation(reservation);

        // Assert
        assertNotNull(createdReservation);
        assertEquals(1L, createdReservation.getId());
        verify(reservationRepository, times(1)).save(reservation);
    }

    @Test
    void testCreateReservationWithNullDetails() {
        // Arrange
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> reservationService.createReservation(reservation));
        verify(reservationRepository, never()).save(reservation);
    }

    @Test
    void testGetReservationByIdSuccess() {
        // Arrange
        long reservationId = 1L;
        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reservation));

        // Act
        Reservation foundReservation = reservationService.getReservationById(reservationId);

        // Assert
        assertNotNull(foundReservation);
        assertEquals(reservationId, foundReservation.getId());
        verify(reservationRepository, times(1)).findById(reservationId);
    }

    @Test
    void testGetReservationByIdNotFound() {
        // Arrange
        long reservationId = 1L;
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationById(reservationId));
        verify(reservationRepository, times(1)).findById(reservationId);
    }
}
