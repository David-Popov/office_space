package javawizzards.officespace.exception.Company;

import javawizzards.officespace.enumerations.Company.CompanyMessages;

public class CompanyCustomException extends RuntimeException{
    protected CompanyCustomException(String message) {
        super(message);
    }

    public static class CompanyNotFoundException extends CompanyCustomException {
        public CompanyNotFoundException() {
            super(CompanyMessages.COMPANY_NOT_FOUND.getMessage());
        }
    }

    public static class InvalidCompanyId extends CompanyCustomException {
        public InvalidCompanyId() {
            super(CompanyMessages.INVALID_COMPANY_ID.getMessage());
        }
    }
}