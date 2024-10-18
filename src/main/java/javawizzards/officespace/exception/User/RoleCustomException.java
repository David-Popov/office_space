package javawizzards.officespace.exception.User;

import javawizzards.officespace.enumerations.User.RoleExceptionMessages;

public class RoleCustomException {

    public static class RoleNotFoundException extends RuntimeException {
        public RoleNotFoundException() {
            super(RoleExceptionMessages.ROLE_NOT_FOUND.getMessage());
        }
    }

    public static class RoleAlreadyExistsException extends RuntimeException {
        public RoleAlreadyExistsException() {
            super(RoleExceptionMessages.ROLE_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidRoleNameException extends RuntimeException {
        public InvalidRoleNameException() {
            super(RoleExceptionMessages.INVALID_ROLE_NAME.getMessage());
        }
    }

    public static class RoleIdNotFoundException extends RuntimeException {
        public RoleIdNotFoundException() {
            super(RoleExceptionMessages.ROLE_ID_NOT_FOUND.getMessage());
        }
    }
}
