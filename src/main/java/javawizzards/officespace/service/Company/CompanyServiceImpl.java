package javawizzards.officespace.service.Company;

import javawizzards.officespace.dto.Company.CompanyDto;
import javawizzards.officespace.dto.Company.UpdateCompanyRequest;
import javawizzards.officespace.entity.Company;
import javawizzards.officespace.entity.Department;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.Department.DepartmentType;
import javawizzards.officespace.exception.Company.CompanyCustomException;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    public CompanyServiceImpl(CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.logger = Logger.getLogger(CompanyServiceImpl.class.getName());
    }

    @Override
    public CompanyDto findCompanyByName(String companyName) {
        try {
            Company company = companyRepository.findByName(companyName)
                    .orElseThrow(CompanyCustomException.CompanyNotFoundException::new);
            return mapToDto(company);
        } catch (Exception e) {
            logger.severe("Error occurred while fetching company by name: " + companyName + " - " + e.getMessage());
            throw e;
        }
    }

    @Override
    public CompanyDto findCompanyById(UUID id) {
        try {
            Company company = companyRepository.findById(id)
                    .orElseThrow(CompanyCustomException.CompanyNotFoundException::new);
            return mapToDto(company);
        } catch (Exception e) {
            logger.severe("Error occurred while fetching company by id: " + id + " - " + e.getMessage());
            throw e;
        }
    }

    @Override
    public CompanyDto createCompany(CompanyDto createCompanyDto) {
        try {
            Company company = modelMapper.map(createCompanyDto, Company.class);

            this.createDepartmentsForCompany(company);

            Company savedCompany = companyRepository.save(company);
            return mapToDto(savedCompany);
        } catch (Exception e) {
            logger.severe("Error occurred while creating company: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public CompanyDto updateCompany(UUID companyId, UpdateCompanyRequest updateCompanyDto) {
        try {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(CompanyCustomException.CompanyNotFoundException::new);

            if(updateCompanyDto.getName() != null) company.setName(updateCompanyDto.getName());
            if(updateCompanyDto.getAddress() != null) company.setAddress(updateCompanyDto.getAddress());
            if(updateCompanyDto.getType() != null) company.setType(updateCompanyDto.getType());

            Company updatedCompany = companyRepository.save(company);
            return mapToDto(updatedCompany);
        } catch (Exception e) {
            logger.severe("Error occurred while updating company ID " + companyId + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCompany(UUID id) {
        try {
            if (!companyRepository.existsById(id)) {
                throw new CompanyCustomException.CompanyNotFoundException();
            }
            companyRepository.deleteById(id);
        } catch (Exception e) {
            logger.severe("Error occurred while deleting company ID " + id + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CompanyDto> getCompaniesByType(String type) {
        try {
            List<Company> companies = companyRepository.findByType(type);
            return companies.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching companies by type: " + type + " - " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CompanyDto> getAllCompanies() {
        try {
            List<Company> companies = companyRepository.findAll();
            return companies.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching all companies: " + e.getMessage());
            throw e;
        }
    }

    private CompanyDto mapToDto(Company company) {
        try {
            return modelMapper.map(company, CompanyDto.class);
        } catch (Exception e) {
            logger.severe("Error occurred while mapping company to DTO: " + e.getMessage());
            throw new RuntimeException("Error mapping company to DTO", e);
        }
    }

    private List<Department> createDepartmentsForCompany(Company company) {
        Map<String, DepartmentType> departmentTypes = Map.of(
                "Human Resources", DepartmentType.HR,
                "Finance", DepartmentType.FINANCE,
                "Marketing", DepartmentType.MARKETING,
                "DevOps", DepartmentType.DEVOPS,
                "IT", DepartmentType.IT_SUPPORT,
                "Sales", DepartmentType.SALES,
                "Research & Development", DepartmentType.RESEARCH_AND_DEVELOPMENT,
                "IT Suport", DepartmentType.IT_SUPPORT
        );

        List<Department> departments = new ArrayList<>();

        List<String> departmentNames = new ArrayList<>(departmentTypes.keySet());
        for (int i = 0; i < departmentTypes.size(); i++) {
            String departmentName = departmentNames.get(i);
            Department department = new Department();
            department.setName(departmentName);
            department.setDepartmentType(departmentTypes.get(departmentName));
            department.setCompany(company);
            department.setUsers(new ArrayList<>());
            departments.add(department);
        }

        return departments;
    }
}