package javawizzards.officespace.Reservation;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Reservation reservation) {
        if (reservation.getDetails() == null || reservation.getDetails().isEmpty()) {
            throw new IllegalArgumentException("Reservation details cannot be null or empty");
        }
        return reservationRepository.save(reservation);
    }

    public Reservation getReservationById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found with ID: " + id));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}
