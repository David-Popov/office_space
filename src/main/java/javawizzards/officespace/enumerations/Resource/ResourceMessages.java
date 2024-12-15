package javawizzards.officespace.enumerations.Resource;

public enum ResourceMessages {
    RESOURCE_NOT_FOUND("Resource not found."),
    OFFICE_ROOM_NOT_FOUND("Office room not found."),
    NO_RESOURCES_FOUND_IN_ROOM("No resources found in this office room."),

    RESOURCE_ALREADY_EXISTS("Resource with this name and type already exists in this room."),
    INVALID_QUANTITY("Resource quantity must be zero or positive."),
    INVALID_RESOURCE_TYPE("Invalid resource type specified."),
    INVALID_RESOURCE_STATUS("Invalid resource status specified."),

    RESOURCE_STATUS_UPDATED("Resource status successfully updated."),
    MAINTENANCE_SCHEDULED("Resource scheduled for maintenance."),
    MAINTENANCE_COMPLETED("Maintenance completed successfully."),

    RESOURCE_CREATED("Resource created successfully."),
    RESOURCE_UPDATED("Resource updated successfully."),
    RESOURCE_DELETED("Resource deleted successfully."),

    RESOURCE_CREATE_ERROR("Error occurred while creating resource."),
    RESOURCE_UPDATE_ERROR("Error occurred while updating resource."),
    RESOURCE_DELETE_ERROR("Error occurred while deleting resource."),

    MAINTENANCE_NOTES_REQUIRED("Maintenance notes are required when putting resource under maintenance."),
    RESOURCE_IN_USE("Cannot modify resource that is currently in use."),
    RESOURCE_UNDER_MAINTENANCE("Cannot modify resource that is under maintenance.");

    private final String message;

    ResourceMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}