package javawizzards.officespace.exception.User;

import javawizzards.officespace.enumerations.User.UserExceptionMessages;

public class UserCustomException {
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException() {
            super(UserExceptionMessages.USER_NOT_FOUND.getMessage());
        }
    }

    public static class GoogleUserNotFoundException extends RuntimeException {
        public GoogleUserNotFoundException() {
            super(UserExceptionMessages.GOOGLE_USER_NOT_FOUND.getMessage());
        }
    }

    public static class EmailAlreadyExistsException extends RuntimeException {
        public EmailAlreadyExistsException() {
            super(UserExceptionMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }
    }

    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException() {
            super(UserExceptionMessages.INVALID_PASSWORD.getMessage());
        }
    }

    public static class UserNotActiveException extends RuntimeException {
        public UserNotActiveException() {
            super(UserExceptionMessages.USER_NOT_ACTIVE.getMessage());
        }
    }

    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException() {
            super(UserExceptionMessages.USERNAME_ALREADY_EXISTS.getMessage());
        }
    }

    public static class UserAlreadyExistsException extends RuntimeException {
        public UserAlreadyExistsException() {
            super(UserExceptionMessages.USER_ALREADY_EXISTS.getMessage());
        }
    }

    public static class UserLockedException extends RuntimeException {
        public UserLockedException() {
            super(UserExceptionMessages.USER_LOCKED.getMessage());
        }
    }

    public static class PasswordTooShortException extends RuntimeException {
        public PasswordTooShortException() {
            super(UserExceptionMessages.PASSWORD_TOO_SHORT.getMessage());
        }
    }

    public static class InvalidEmailFormatException extends RuntimeException {
        public InvalidEmailFormatException() {
            super(UserExceptionMessages.INVALID_EMAIL_FORMAT.getMessage());
        }
    }

    public static class UserNotVerifiedException extends RuntimeException {
        public UserNotVerifiedException() {
            super(UserExceptionMessages.USER_NOT_VERIFIED.getMessage());
        }
    }

    public static class PasswordMismatchException extends RuntimeException {
        public PasswordMismatchException() {
            super(UserExceptionMessages.PASSWORD_MISMATCH.getMessage());
        }
    }

    public static class UserAlreadyLoggedInException extends RuntimeException {
        public UserAlreadyLoggedInException() {
            super(UserExceptionMessages.USER_ALREADY_LOGGED_IN.getMessage());
        }
    }

    public static class RoleNotFoundException extends RuntimeException {
        public RoleNotFoundException() {
            super(UserExceptionMessages.ROLE_NOT_FOUND.getMessage());
        }
    }

    public static class UserDeletedException extends RuntimeException {
        public UserDeletedException() {
            super(UserExceptionMessages.USER_DELETED.getMessage());
        }
    }

    public static class RegisterFailed extends RuntimeException {
        public RegisterFailed() {
            super(UserExceptionMessages.REGISTER_FAILED.getMessage());
        }
    }
}

