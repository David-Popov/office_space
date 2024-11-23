package javawizzards.officespace.exception.Resource;

import javawizzards.officespace.enumerations.Resource.ResourceMessages;

public class ResourceCustomException extends RuntimeException {

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException() {
            super(ResourceMessages.RESOURCE_NOT_FOUND.getMessage());
        }
    }

    public static class ResourceAlreadyExistsException extends RuntimeException {
        public ResourceAlreadyExistsException() {
            super(ResourceMessages.RESOURCE_ALREADY_EXISTS.getMessage());
        }
    }
}