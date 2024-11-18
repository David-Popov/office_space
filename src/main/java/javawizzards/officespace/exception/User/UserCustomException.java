package javawizzards.officespace.exception.User;

import javawizzards.officespace.enumerations.User.UserMessages;

public class UserCustomException {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super(UserMessages.USER_NOT_FOUND.getMessage());
        }
    }

    public static class GoogleUserNotFoundException extends RuntimeException {
        public GoogleUserNotFoundException() {
            super(UserMessages.GOOGLE_USER_NOT_FOUND.getMessage());
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException() {
            super(UserMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException() {
            super(UserMessages.INVALID_PASSWORD.getMessage());
        }
    }

    public static class UserNotActiveException extends RuntimeException {
        public UserNotActiveException() {
            super(UserMessages.USER_NOT_ACTIVE.getMessage());
        }
    }

    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException() {
            super(UserMessages.USERNAME_ALREADY_EXISTS.getMessage());
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException() {
            super(UserMessages.USER_ALREADY_EXISTS.getMessage());
        }
    }

    public static class UserLockedException extends RuntimeException {
        public UserLockedException() {
            super(UserMessages.USER_LOCKED.getMessage());
        }
    }

    public static class PasswordTooShortException extends RuntimeException {
        public PasswordTooShortException() {
            super(UserMessages.PASSWORD_TOO_SHORT.getMessage());
        }
    }

    public static class InvalidEmailFormatException extends RuntimeException {
        public InvalidEmailFormatException() {
            super(UserMessages.INVALID_EMAIL_FORMAT.getMessage());
        }
    }

    public static class UserNotVerifiedException extends RuntimeException {
        public UserNotVerifiedException() {
            super(UserMessages.USER_NOT_VERIFIED.getMessage());
        }
    }

    public static class PasswordMismatchException extends RuntimeException {
        public PasswordMismatchException() {
            super(UserMessages.PASSWORD_MISMATCH.getMessage());
        }
    }

    public static class UserAlreadyLoggedInException extends RuntimeException {
        public UserAlreadyLoggedInException() {
            super(UserMessages.USER_ALREADY_LOGGED_IN.getMessage());
        }
    }

    public static class RoleNotFoundException extends RuntimeException {
        public RoleNotFoundException() {
            super(UserMessages.ROLE_NOT_FOUND.getMessage());
        }
    }

    public static class UserDeletedException extends RuntimeException {
        public UserDeletedException() {
            super(UserMessages.USER_DELETED.getMessage());
        }
    }

    public static class RegisterFailed extends RuntimeException {
        public RegisterFailed() {
            super(UserMessages.REGISTER_FAILED.getMessage());
        }
    }
}

