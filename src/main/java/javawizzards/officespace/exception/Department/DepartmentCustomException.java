package javawizzards.officespace.exception.Department;

import javawizzards.officespace.enumerations.Department.DepartmentMessages;

public class DepartmentCustomException {

    public static class DepartmentNotFoundException extends RuntimeException {
        public DepartmentNotFoundException() {
            super(DepartmentMessages.DEPARTMENT_NOT_FOUND.getMessage());
        }
    }
}