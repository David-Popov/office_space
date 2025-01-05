package javawizzards.officespace.service.Company;

import javawizzards.officespace.dto.Company.*;
import java.util.List;
import java.util.UUID;

public interface CompanyService {
    CompanyDto createCompany(CompanyDto createCompanyDto);
    CompanyDto updateCompany(UUID id, UpdateCompanyRequest updateCompanyDto);
    void deleteCompany(UUID id);
    CompanyDto findCompanyByName(String name);
    CompanyDto findCompanyById(UUID id);
    List<CompanyDto> getCompaniesByType(String type); 
    List<CompanyDto> getAllCompanies(); 
}