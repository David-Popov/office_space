package javawizzards.officespace.exception.Employee;

import javawizzards.officespace.enumerations.Employee.EmployeeMessages;

public class EmployeeCustomException extends RuntimeException{
    protected EmployeeCustomException(String message) {
        super(message);
    }

    public static class EmployeeNotFoundException extends EmployeeCustomException {
        public EmployeeNotFoundException() {
            super(EmployeeMessages.EMPLOYEE_NOT_FOUND.getMessage());
        }
    }
}