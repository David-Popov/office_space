package javawizzards.officespace.enumerations.GoogleCalendar;

public enum GoogleCalendarMessages {

    ERROR_GETTING_GOOGLE_CALENDAR_SERVICE("Error to get Google Calendar Service"),
    ERROR_GETTING_GOOGLE_API_CREDENTIALS("Error get Google Api Client OAuth Credential"),
    ERROR_LOADING_GOOGLE_CLIENT_SECRETS("Error to get GoogleClientSecrets"),
    RESOURCE_NOT_FOUND_GOOGLE_CREDENTIALS("Resource not found: google-credentials.json");

    private final String message;

    GoogleCalendarMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
