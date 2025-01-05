package javawizzards.officespace.enumerations.Company;

public enum CompanyMessages {
    COMPANY_NOT_FOUND("Company not found."),
    INVALID_COMPANY_ID("Invalid Company Id."),
    COMPANY_NAME_REQUIRED("Company name is required."),
    COMPANY_ALREADY_EXISTS("Company with this name already exists."),
    INVALID_COMPANY_DATA("Invalid company data provided."),
    OPERATION_FAILED("Operation failed. Please try again."),

    COMPANY_CREATED_SUCCESSFULLY("Company created successfully."),
    COMPANY_UPDATED_SUCCESSFULLY("Company details updated successfully."),
    COMPANY_DELETED_SUCCESSFULLY("Company deleted successfully."),
    COMPANY_RESTORED_SUCCESSFULLY("Company restored successfully."),
    COMPANY_STATUS_UPDATED("Company status updated successfully."),

    INVALID_EMAIL_FORMAT("Company email format is invalid."),
    INVALID_PHONE_FORMAT("Company phone number format is invalid."),
    INVALID_ADDRESS("Company address information is incomplete or invalid.");

    private final String message;

    CompanyMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}