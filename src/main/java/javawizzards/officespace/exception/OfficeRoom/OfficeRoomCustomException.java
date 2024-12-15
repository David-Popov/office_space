package javawizzards.officespace.exception.OfficeRoom;

import javawizzards.officespace.enumerations.OfficeRoom.OfficeRoomMessages;

public abstract class OfficeRoomCustomException extends RuntimeException {
    protected OfficeRoomCustomException(String message) {
        super(message);
    }

    public static class OfficeRoomNotFoundException extends OfficeRoomCustomException {
        public OfficeRoomNotFoundException() {
            super(OfficeRoomMessages.OFFICE_ROOM_NOT_FOUND.getMessage());
        }
    }

    public static class OfficeRoomCreationFailedException extends OfficeRoomCustomException {
        public OfficeRoomCreationFailedException() {
            super(OfficeRoomMessages.OFFICE_ROOM_CREATION_FAILED.getMessage());
        }
    }

    public static class OfficeRoomUpdateFailedException extends OfficeRoomCustomException {
        public OfficeRoomUpdateFailedException() {
            super(OfficeRoomMessages.OFFICE_ROOM_UPDATE_FAILED.getMessage());
        }
    }

    public static class OfficeRoomDeletionFailedException extends OfficeRoomCustomException {
        public OfficeRoomDeletionFailedException() {
            super(OfficeRoomMessages.OFFICE_ROOM_DELETE_FAILED.getMessage());
        }
    }

    public static class OfficeRoomAlreadyExistsException extends OfficeRoomCustomException {
        public OfficeRoomAlreadyExistsException() {
            super(OfficeRoomMessages.OFFICE_ROOM_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidOfficeRoomDataException extends OfficeRoomCustomException {
        public InvalidOfficeRoomDataException() {
            super(OfficeRoomMessages.INVALID_OFFICE_ROOM_DATA.getMessage());
        }
    }

    public static class InvalidCapacityException extends OfficeRoomCustomException {
        public InvalidCapacityException() {
            super(OfficeRoomMessages.INVALID_CAPACITY.getMessage());
        }
    }

    public static class InvalidPriceException extends OfficeRoomCustomException {
        public InvalidPriceException() {
            super(OfficeRoomMessages.INVALID_PRICE.getMessage());
        }
    }

    public static class InvalidFloorException extends OfficeRoomCustomException {
        public InvalidFloorException() {
            super(OfficeRoomMessages.INVALID_FLOOR.getMessage());
        }
    }

    public static class InvalidRoomTypeException extends OfficeRoomCustomException {
        public InvalidRoomTypeException() {
            super(OfficeRoomMessages.INVALID_ROOM_TYPE.getMessage());
        }
    }

    public static class InvalidRoomStatusException extends OfficeRoomCustomException {
        public InvalidRoomStatusException() {
            super(OfficeRoomMessages.INVALID_ROOM_STATUS.getMessage());
        }
    }

    public static class RoomNotAvailableException extends OfficeRoomCustomException {
        public RoomNotAvailableException() {
            super(OfficeRoomMessages.ROOM_NOT_AVAILABLE.getMessage());
        }
    }

    public static class ResourceOperationFailedException extends OfficeRoomCustomException {
        public ResourceOperationFailedException(String operation) {
            super(operation.equals("add") ?
                    OfficeRoomMessages.RESOURCE_ADDITION_FAILED.getMessage() :
                    OfficeRoomMessages.RESOURCE_REMOVAL_FAILED.getMessage());
        }
    }

    public static class CompanyNotFoundException extends OfficeRoomCustomException {
        public CompanyNotFoundException() {
            super(OfficeRoomMessages.COMPANY_NOT_FOUND.getMessage());
        }
    }

    public static class ReservationConflictException extends OfficeRoomCustomException {
        public ReservationConflictException() {
            super(OfficeRoomMessages.RESERVATION_CONFLICT.getMessage());
        }
    }
}