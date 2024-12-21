package javawizzards.officespace.exception.Notification;

import javawizzards.officespace.enumerations.Notification.NotificationMessages;

public class NotificationCustomException extends RuntimeException{
    protected NotificationCustomException(String message) {
        super(message);
    }

    public static class NotificationNotFoundException extends NotificationCustomException {
        public NotificationNotFoundException() {
            super(NotificationMessages.NOTIFICATION_NOT_FOUND.getMessage());
        }
    }

    public static class NotificationNotCreatedException extends NotificationCustomException {
        public NotificationNotCreatedException() {
            super(NotificationMessages.NOTIFICATION_NOT_CREATED.getMessage());
        }
    }
    
}
