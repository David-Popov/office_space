package javawizzards.officespace.exception.OfficeRoom;

import javawizzards.officespace.enumerations.OfficeRoom.OfficeRoomMessages;

public class OfficeRoomCustomException {

    public static class OfficeRoomNotFoundException extends RuntimeException {
        public OfficeRoomNotFoundException() {
            super(OfficeRoomMessages.OFFICE_ROOM_NOT_FOUND.getMessage());
        }
    }
}