package javawizzards.officespace.exception.Reservation;

import javawizzards.officespace.enumerations.Reservation.ReservationMessages;

public class ReservationCustomException {

    public static class ReservationNotFoundException extends RuntimeException {
        public ReservationNotFoundException() {
            super(ReservationMessages.RESERVATION_NOT_FOUND.getMessage());
        }
    }

    public static class ReservationConflictException extends RuntimeException {
        public ReservationConflictException() {
            super(ReservationMessages.RESERVATION_CONFLICT.getMessage());
        }
    }

    public static class InvalidReservationDateException extends RuntimeException {
        public InvalidReservationDateException() {
            super(ReservationMessages.INVALID_RESERVATION_DATE.getMessage());
        }
    }
}