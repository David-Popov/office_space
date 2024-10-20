package javawizzards.officespace.enumerations.User;

public enum RoleMessages {
    ROLE_NOT_FOUND("Role not found."),
    ROLE_ALREADY_EXISTS("Role already exists."),
    INVALID_ROLE_NAME("Role name is invalid."),
    ROLE_ID_NOT_FOUND("Role ID not found.");

    private final String message;

    RoleMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
