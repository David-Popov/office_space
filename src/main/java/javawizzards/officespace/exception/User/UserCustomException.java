package javawizzards.officespace.exception.User;

import javawizzards.officespace.enumerations.User.UserMessages;

public abstract class UserCustomException extends RuntimeException {
    protected UserCustomException(String message) {
        super(message);
    }

    public static class UserNotFoundException extends UserCustomException {
        public UserNotFoundException() {
            super(UserMessages.USER_NOT_FOUND.getMessage());
        }
    }

    public static class InvalidId extends UserCustomException {
        public InvalidId() {
            super(UserMessages.INVALID_ID.getMessage());
        }
    }

    public static class GoogleUserNotFoundException extends UserCustomException {
        public GoogleUserNotFoundException() {
            super(UserMessages.GOOGLE_USER_NOT_FOUND.getMessage());
        }
    }

    public static class EmailAlreadyExistsException extends UserCustomException {
        public EmailAlreadyExistsException() {
            super(UserMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidPasswordException extends UserCustomException {
        public InvalidPasswordException() {
            super(UserMessages.INVALID_PASSWORD.getMessage());
        }
    }

    public static class UserNotActiveException extends UserCustomException {
        public UserNotActiveException() {
            super(UserMessages.USER_NOT_ACTIVE.getMessage());
        }
    }

    public static class UsernameAlreadyExistsException extends UserCustomException {
        public UsernameAlreadyExistsException() {
            super(UserMessages.USERNAME_ALREADY_EXISTS.getMessage());
        }
    }

    public static class UserAlreadyExistsException extends UserCustomException {
        public UserAlreadyExistsException() {
            super(UserMessages.USER_ALREADY_EXISTS.getMessage());
        }
    }

    public static class UserLockedException extends UserCustomException {
        public UserLockedException() {
            super(UserMessages.USER_LOCKED.getMessage());
        }
    }

    public static class PasswordTooShortException extends UserCustomException {
        public PasswordTooShortException() {
            super(UserMessages.PASSWORD_TOO_SHORT.getMessage());
        }
    }

    public static class InvalidEmailFormatException extends UserCustomException {
        public InvalidEmailFormatException() {
            super(UserMessages.INVALID_EMAIL_FORMAT.getMessage());
        }
    }

    public static class UserNotVerifiedException extends UserCustomException {
        public UserNotVerifiedException() {
            super(UserMessages.USER_NOT_VERIFIED.getMessage());
        }
    }

    public static class PasswordMismatchException extends UserCustomException {
        public PasswordMismatchException() {
            super(UserMessages.PASSWORD_MISMATCH.getMessage());
        }
    }

    public static class UserAlreadyLoggedInException extends UserCustomException {
        public UserAlreadyLoggedInException() {
            super(UserMessages.USER_ALREADY_LOGGED_IN.getMessage());
        }
    }

    public static class RoleNotFoundException extends UserCustomException {
        public RoleNotFoundException() {
            super(UserMessages.ROLE_NOT_FOUND.getMessage());
        }
    }

    public static class UserDeletedException extends UserCustomException {
        public UserDeletedException() {
            super(UserMessages.USER_DELETED.getMessage());
        }
    }

    public static class RegisterFailed extends UserCustomException {
        public RegisterFailed() {
            super(UserMessages.REGISTER_FAILED.getMessage());
        }
    }
}

