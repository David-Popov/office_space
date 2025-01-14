package javawizzards.officespace.exception.GoogleCalendar;

public class GoogleCalendarLibException extends Exception {

    private static final long serialVersionUID = 4682053954814519272L;

    public GoogleCalendarLibException(String message, Object... args) {
        super(String.format(message, args));
    }

    public GoogleCalendarLibException(String message) {
        super(message);
    }
}
