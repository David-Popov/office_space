package javawizzards.officespace.enumerations.Department;

public enum DepartmentMessages {
    DEPARTMENT_NOT_FOUND("Department not found.");

    private final String message;

    DepartmentMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}