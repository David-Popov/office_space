package javawizzards.officespace.enumerations.OfficeRoom;

public enum OfficeRoomMessages {
    OFFICE_ROOM_NOT_FOUND("OfficeRoom not found."),
    OFFICE_ROOM_CREATION_FAILED("OfficeRoom creation failed."),
    OFFICE_ROOM_CREATION_SUCCESS("OfficeRoom created successfully");

    private final String message;

    OfficeRoomMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}