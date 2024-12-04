package javawizzards.officespace.service.Reservation;

import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import java.util.List;
import java.util.UUID;

public interface ReservationService {
    ReservationDto createReservation(CreateReservationDto reservationDto);
    ReservationDto updateReservation(UUID reservationId, ReservationDto reservationDto);
    void deleteReservation(UUID reservationId);
    ReservationDto findReservationById(UUID reservationId);
    List<ReservationDto> findReservationsByOfficeRoomId(UUID officeRoomId);
    List<ReservationDto> findReservationsByUserId(UUID userId);
    List<String> getReservationStatusList();
}