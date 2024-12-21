package javawizzards.officespace.enumerations.OfficeRoom;

public enum OfficeRoomMessages {
    OFFICE_ROOM_NOT_FOUND("OfficeRoom not found."),
    OFFICE_ROOM_CREATION_FAILED("OfficeRoom creation failed."),
    OFFICE_ROOM_CREATION_SUCCESS("OfficeRoom created successfully."),
    OFFICE_ROOM_UPDATE_FAILED("OfficeRoom update failed."),
    OFFICE_ROOM_UPDATE_SUCCESS("OfficeRoom updated successfully."),
    OFFICE_ROOM_DELETE_FAILED("OfficeRoom deletion failed."),
    OFFICE_ROOM_DELETE_SUCCESS("OfficeRoom deleted successfully."),
    OFFICE_ROOM_ALREADY_EXISTS("OfficeRoom already exists."),
    INVALID_OFFICE_ROOM_DATA("Invalid office room data provided."),
    INVALID_CAPACITY("Invalid capacity value provided."),
    INVALID_PRICE("Invalid price value provided."),
    INVALID_FLOOR("Invalid floor value provided."),
    INVALID_ROOM_TYPE("Invalid room type provided."),
    INVALID_ROOM_STATUS("Invalid room status provided."),
    ROOM_NOT_AVAILABLE("Room is not available for the requested time period."),
    RESOURCE_ADDITION_FAILED("Failed to add resource to office room."),
    RESOURCE_REMOVAL_FAILED("Failed to remove resource from office room."),
    COMPANY_NOT_FOUND("Company not found for office room."),
    RESERVATION_CONFLICT("Room has conflicting reservations."),

    //success
    OFFICE_ROOM_FETCH_SUCCESS("Office room fetched successfully"),
    OFFICE_ROOMS_FETCH_SUCCESS("Office rooms fetched successfully"),
    RESOURCE_ADDED_SUCCESS("Resource added successfully"),
    RESOURCES_ADDED_SUCCESS("Resources added successfully"),
    RESOURCE_REMOVED_SUCCESS("Resource removed successfully"),
    STATUSES_FETCH_SUCCESS("Statuses fetched successfully"),
    TYPES_FETCH_SUCCESS("Types fetched successfully"),
    CUSTOM_ERROR("Custom office room error occurred");

    private final String message;

    OfficeRoomMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}