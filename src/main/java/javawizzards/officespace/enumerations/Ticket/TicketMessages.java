package javawizzards.officespace.enumerations.Ticket;

public enum TicketMessages {
    TICKET_CREATION_FAILED("Ticket creation failed."),
    TICKET_NOT_FOUND("Ticket not found."),
    INVALID_TICKET_STATUS("Invalid ticket status."),
    TICKET_DELETION_FAILED("Ticket deletion failed."),
    TICKET_DATA_INVALID("Ticket data is invalid."),
    TICKET_DELETE_SUCCESS("Ticket deleted successfully."),
    TICKET_USER_NOT_FOUND("User not found."),
    TICKET_CREATION_SUCCESS("Ticket created successfully."),
    TICKET_UPDATE_SUCCESS("Ticket updated successfully."),
    TICKET_STATUS_CHANGE_SUCCESS("Ticket status changed successfully.");

    private final String message;

    TicketMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
