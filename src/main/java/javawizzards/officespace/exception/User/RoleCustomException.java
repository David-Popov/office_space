package javawizzards.officespace.exception.User;

import javawizzards.officespace.enumerations.User.RoleMessages;

public class RoleCustomException {

    public static class RoleNotFoundException extends RuntimeException {
        public RoleNotFoundException() {
            super(RoleMessages.ROLE_NOT_FOUND.getMessage());
        }
    }

    public static class RoleAlreadyExistsException extends RuntimeException {
        public RoleAlreadyExistsException() {
            super(RoleMessages.ROLE_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidRoleNameException extends RuntimeException {
        public InvalidRoleNameException() {
            super(RoleMessages.INVALID_ROLE_NAME.getMessage());
        }
    }

    public static class RoleIdNotFoundException extends RuntimeException {
        public RoleIdNotFoundException() {
            super(RoleMessages.ROLE_ID_NOT_FOUND.getMessage());
        }
    }
}
