package javawizzards.officespace.enumerations.Company;

public enum CompanyMessages {
    COMPANY_NOT_FOUND("Company not found.");

    private final String message;

    CompanyMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}