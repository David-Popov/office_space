package javawizzards.officespace.enumerations.Resource;

public enum ResourceMessages {
    RESOURCE_NOT_FOUND("Resource not found."),
    RESOURCE_ALREADY_EXISTS("Resource already exists.");

    private final String message;

    ResourceMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}