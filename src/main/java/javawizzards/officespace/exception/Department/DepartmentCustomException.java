package javawizzards.officespace.exception.Department;

import javawizzards.officespace.enumerations.Department.DepartmentMessages;

public class DepartmentCustomException extends RuntimeException{
    protected DepartmentCustomException(String message) {
        super(message);
    }

    public static class DepartmentNotFoundException extends DepartmentCustomException {
        public DepartmentNotFoundException() {
            super(DepartmentMessages.DEPARTMENT_NOT_FOUND.getMessage());
        }
    }
}