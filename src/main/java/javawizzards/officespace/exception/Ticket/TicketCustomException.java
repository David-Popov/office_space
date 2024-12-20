package javawizzards.officespace.exception.Ticket;

import javawizzards.officespace.enumerations.Ticket.TicketMessages;

public abstract class TicketCustomException extends RuntimeException {
    protected TicketCustomException(String message) {
        super(message);
    }

    public static class TicketCreationException extends TicketCustomException {
        public TicketCreationException() {
            super(TicketMessages.TICKET_CREATION_FAILED.getMessage());
        }
    }

    public static class TicketNotFoundException extends TicketCustomException {
        public TicketNotFoundException() {
            super(TicketMessages.TICKET_NOT_FOUND.getMessage());
        }
    }

    public static class InvalidTicketStatusException extends TicketCustomException {
        public InvalidTicketStatusException() {
            super(TicketMessages.INVALID_TICKET_STATUS.getMessage());
        }
    }

    public static class TicketDeletionException extends TicketCustomException {
        public TicketDeletionException() {
            super(TicketMessages.TICKET_DELETION_FAILED.getMessage());
        }
    }

    public static class TicketDeleteSuccessException extends TicketCustomException {
        public TicketDeleteSuccessException() {
            super(TicketMessages.TICKET_DELETE_SUCCESS.getMessage());
        }
    }
    public static class TicketDataInvalidException extends TicketCustomException {
        public TicketDataInvalidException() {
            super(TicketMessages.TICKET_DATA_INVALID.getMessage());
        }
    }

    public static class TicketUserNotFoundException extends TicketCustomException {
        public TicketUserNotFoundException() {
            super(TicketMessages.TICKET_USER_NOT_FOUND.getMessage());
        }
    }
}
