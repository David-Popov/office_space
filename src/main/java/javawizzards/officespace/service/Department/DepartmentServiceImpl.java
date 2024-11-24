package javawizzards.officespace.service.Department;

import javawizzards.officespace.dto.Department.DepartmentDto;
import javawizzards.officespace.entity.Company;
import javawizzards.officespace.entity.Department;
import javawizzards.officespace.exception.Department.DepartmentCustomException;
import javawizzards.officespace.exception.Company.CompanyCustomException;
import javawizzards.officespace.repository.DepartmentRepository;
import javawizzards.officespace.repository.CompanyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository, CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.departmentRepository = departmentRepository;
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.logger = Logger.getLogger(DepartmentServiceImpl.class.getName());
    }

    @Override
    public DepartmentDto createDepartment(DepartmentDto createDepartmentDto) {
        try {
            Company company = companyRepository.findByName(createDepartmentDto.getCompanyName())
                .orElseThrow(() -> new CompanyCustomException.CompanyNotFoundException());

            Department department = modelMapper.map(createDepartmentDto, Department.class);
            department.setCompany(company);

            Department savedDepartment = departmentRepository.save(department);
            return mapToDto(savedDepartment);
        } catch (Exception e) {
            logger.severe("Error occurred while creating department: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public DepartmentDto updateDepartment(UUID departmentId, DepartmentDto updateDepartmentDto) {
        try {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(DepartmentCustomException.DepartmentNotFoundException::new);

            if(updateDepartmentDto.getName() != null) department.setName(updateDepartmentDto.getName());

            if(updateDepartmentDto.getCompanyName() != null) {
                Company newCompany = companyRepository.findByName(updateDepartmentDto.getCompanyName())
                    .orElseThrow(() -> new CompanyCustomException.CompanyNotFoundException());
                department.setCompany(newCompany);
            }

            Department updatedDepartment = departmentRepository.save(department);
            return mapToDto(updatedDepartment);
        } catch (Exception e) {
            logger.severe("Error occurred while updating department ID " + departmentId + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteDepartment(UUID departmentId) {
        try {
            Department department = departmentRepository.findById(departmentId)
                    .orElseThrow(DepartmentCustomException.DepartmentNotFoundException::new);

            departmentRepository.delete(department);
        } catch (Exception e) {
            logger.severe("Error occurred while deleting department ID " + departmentId + ": " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<DepartmentDto> findDepartmentsByCompanyName(String companyName) {
        try {
            List<Department> departments = departmentRepository.findByCompanyName(companyName);
            return departments.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching departments by company name: " + companyName + " - " + e.getMessage());
            throw e;
        }
    }

    @Override
    public DepartmentDto findDepartmentByName(String departmentName) {
        try {
            Department department = departmentRepository.findByName(departmentName)
                    .orElseThrow(DepartmentCustomException.DepartmentNotFoundException::new);

            return mapToDto(department);
        } catch (Exception e) {
            logger.severe("Error occurred while fetching department by name: " + departmentName + " - " + e.getMessage());
            throw e;
        }
    }

    private DepartmentDto mapToDto(Department department) {
        try {
            return modelMapper.map(department, DepartmentDto.class);
        } catch (Exception e) {
            logger.severe("Error occurred while mapping department to DTO: " + e.getMessage());
            throw new RuntimeException("Error mapping department to DTO", e);
        }
    }
}