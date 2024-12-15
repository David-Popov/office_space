package javawizzards.officespace.exception.User;

import javawizzards.officespace.enumerations.User.RoleMessages;

public abstract class RoleCustomException extends RuntimeException{
    protected RoleCustomException(String message) {
        super(message);
    }

    public static class RoleNotFoundException extends RoleCustomException {
        public RoleNotFoundException() {
            super(RoleMessages.ROLE_NOT_FOUND.getMessage());
        }
    }

    public static class RoleAlreadyExistsException extends RoleCustomException {
        public RoleAlreadyExistsException() {
            super(RoleMessages.ROLE_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidRoleNameException extends RoleCustomException {
        public InvalidRoleNameException() {
            super(RoleMessages.INVALID_ROLE_NAME.getMessage());
        }
    }

    public static class RoleIdNotFoundException extends RoleCustomException {
        public RoleIdNotFoundException() {
            super(RoleMessages.ROLE_ID_NOT_FOUND.getMessage());
        }
    }
}
