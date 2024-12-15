package javawizzards.officespace.exception.Reservation;

import javawizzards.officespace.enumerations.Reservation.ReservationMessages;

public abstract class ReservationCustomException extends RuntimeException {
    protected ReservationCustomException(String message) {
        super(message);
    }

    public static class ReservationNotFoundException extends ReservationCustomException {
        public ReservationNotFoundException() {
            super(ReservationMessages.RESERVATION_NOT_FOUND.getMessage());
        }
    }

    public static class ReservationConflictException extends ReservationCustomException {
        public ReservationConflictException() {
            super(ReservationMessages.RESERVATION_CONFLICT.getMessage());
        }
    }

    public static class InvalidReservationDateException extends ReservationCustomException {
        public InvalidReservationDateException() {
            super(ReservationMessages.INVALID_RESERVATION_DATE.getMessage());
        }
    }
}