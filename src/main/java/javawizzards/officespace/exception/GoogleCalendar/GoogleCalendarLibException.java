package javawizzards.officespace.exception.GoogleCalendar;

import javawizzards.officespace.enumerations.GoogleCalendar.GoogleCalendarMessages;
import javawizzards.officespace.exception.Department.DepartmentCustomException;

public class GoogleCalendarLibException extends Exception {

    private static final long serialVersionUID = 4682053954814519272L;

    public GoogleCalendarLibException(String message, Object... args) {
        super(String.format(message, args));
    }

    public GoogleCalendarLibException(String message) {
        super(message);
    }


    public static class ErrorGettingGoogleCalendarService extends GoogleCalendarLibException {
        public ErrorGettingGoogleCalendarService() {
            super(GoogleCalendarMessages.ERROR_GETTING_GOOGLE_CALENDAR_SERVICE.getMessage());
        }
    }

    public static class ErrorGettingGoogleApiCredentials extends GoogleCalendarLibException {
        public ErrorGettingGoogleApiCredentials() {
            super(GoogleCalendarMessages.ERROR_GETTING_GOOGLE_API_CREDENTIALS.getMessage());
        }
    }

    public static class ErrorLoadingGoogleClientSecrets extends GoogleCalendarLibException {
        public ErrorLoadingGoogleClientSecrets() {
            super(GoogleCalendarMessages.ERROR_LOADING_GOOGLE_CLIENT_SECRETS.getMessage());
        }
    }

    public static class ResourceNotFoundGoogleCredentials extends GoogleCalendarLibException {
        public ResourceNotFoundGoogleCredentials() {
            super(GoogleCalendarMessages.RESOURCE_NOT_FOUND_GOOGLE_CREDENTIALS.getMessage());
        }
    }

    public static class CustomGoogleCalendarException extends DepartmentCustomException {
        public CustomGoogleCalendarException() {
            super(GoogleCalendarMessages.RESOURCE_NOT_FOUND_GOOGLE_CREDENTIALS.getMessage());
        }
    }
}
