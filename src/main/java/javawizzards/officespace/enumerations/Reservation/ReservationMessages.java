package javawizzards.officespace.enumerations.Reservation;

public enum ReservationMessages {
    RESERVATION_NOT_FOUND("Reservation not found."),
    RESERVATION_CONFLICT("Reservation conflicts with an existing booking."),
    INVALID_RESERVATION_DATE("Invalid reservation dates."),
    RESERVATION_FAILED("Reservation creation failed"),
    RESERVATION_SUCCESS("Reservation created successfully"),
    RESERVATION_UPDATE_SUCCESS("Reservation updated successfully"),
    RESERVATION_DELETE_SUCCESS("Reservation deleted successfully");

    private final String message;

    ReservationMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}