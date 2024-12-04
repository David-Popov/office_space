package javawizzards.officespace.enumerations.Employee;

public enum EmployeeMessages {
    EMPLOYEE_NOT_FOUND("Employee not found.");

    private final String message;

    EmployeeMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}