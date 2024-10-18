package javawizzards.officespace.enumerations.User;

public enum RoleExceptionMessages {
    ROLE_NOT_FOUND("Role not found."),
    ROLE_ALREADY_EXISTS("Role already exists."),
    INVALID_ROLE_NAME("Role name is invalid."),
    ROLE_ID_NOT_FOUND("Role ID not found.");

    private final String message;

    RoleExceptionMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
