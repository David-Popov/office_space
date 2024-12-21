package javawizzards.officespace.exception.Resource;

import javawizzards.officespace.enumerations.Resource.ResourceMessages;

public abstract class ResourceCustomException extends RuntimeException {
    protected ResourceCustomException(String message) {
        super(message);
    }

    // Not Found Exceptions
    public static class ResourceNotFoundException extends ResourceCustomException {
        public ResourceNotFoundException() {
            super(ResourceMessages.RESOURCE_NOT_FOUND.getMessage());
        }
    }

    public static class OfficeRoomNotFoundException extends ResourceCustomException {
        public OfficeRoomNotFoundException() {
            super(ResourceMessages.OFFICE_ROOM_NOT_FOUND.getMessage());
        }
    }

    public static class NoResourcesInRoomException extends ResourceCustomException {
        public NoResourcesInRoomException() {
            super(ResourceMessages.NO_RESOURCES_FOUND_IN_ROOM.getMessage());
        }
    }

    // Validation Exceptions
    public static class ResourceAlreadyExistsException extends ResourceCustomException {
        public ResourceAlreadyExistsException() {
            super(ResourceMessages.RESOURCE_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidQuantityException extends ResourceCustomException {
        public InvalidQuantityException() {
            super(ResourceMessages.INVALID_QUANTITY.getMessage());
        }
    }

    public static class InvalidResourceTypeException extends ResourceCustomException {
        public InvalidResourceTypeException() {
            super(ResourceMessages.INVALID_RESOURCE_TYPE.getMessage());
        }
    }

    public static class InvalidResourceStatusException extends ResourceCustomException {
        public InvalidResourceStatusException() {
            super(ResourceMessages.INVALID_RESOURCE_STATUS.getMessage());
        }
    }

    public static class ResourceInUseException extends ResourceCustomException {
        public ResourceInUseException() {
            super(ResourceMessages.RESOURCE_IN_USE.getMessage());
        }
    }

    public static class ResourceUnderMaintenanceException extends ResourceCustomException {
        public ResourceUnderMaintenanceException() {
            super(ResourceMessages.RESOURCE_UNDER_MAINTENANCE.getMessage());
        }
    }

    public static class MaintenanceNotesRequiredException extends ResourceCustomException {
        public MaintenanceNotesRequiredException() {
            super(ResourceMessages.MAINTENANCE_NOTES_REQUIRED.getMessage());
        }
    }

    public static class ResourceCreateException extends ResourceCustomException {
        public ResourceCreateException() {
            super(ResourceMessages.RESOURCE_CREATE_ERROR.getMessage());
        }
    }

    public static class ResourceUpdateException extends ResourceCustomException {
        public ResourceUpdateException() {
            super(ResourceMessages.RESOURCE_UPDATE_ERROR.getMessage());
        }
    }

    public static class ResourceDeleteException extends ResourceCustomException {
        public ResourceDeleteException() {
            super(ResourceMessages.RESOURCE_DELETE_ERROR.getMessage());
        }
    }
}