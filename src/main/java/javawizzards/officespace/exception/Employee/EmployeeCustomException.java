package javawizzards.officespace.exception.Employee;

import javawizzards.officespace.enumerations.Employee.EmployeeMessages;

public class EmployeeCustomException {

    public static class EmployeeNotFoundException extends RuntimeException {
        public EmployeeNotFoundException() {
            super(EmployeeMessages.EMPLOYEE_NOT_FOUND.getMessage());
        }
    }
}