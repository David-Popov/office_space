package javawizzards.officespace.exception.Company;

import javawizzards.officespace.enumerations.Company.CompanyMessages;

public class CompanyCustomException {

    public static class CompanyNotFoundException extends RuntimeException {
        public CompanyNotFoundException() {
            super(CompanyMessages.COMPANY_NOT_FOUND.getMessage());
        }
    }
}