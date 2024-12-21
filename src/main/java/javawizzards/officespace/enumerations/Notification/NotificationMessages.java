package javawizzards.officespace.enumerations.Notification;

public enum NotificationMessages {
    NOTIFICATION_NOT_FOUND ("Notification not found"),
    NOTIFICATION_NOT_CREATED ("Notification not created");

    private final String message;

    NotificationMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
