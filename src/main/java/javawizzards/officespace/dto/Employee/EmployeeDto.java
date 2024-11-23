package javawizzards.officespace.dto.Employee;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Getter
@Setter
public class EmployeeDto implements Serializable {
    @NotNull(message = "Employee name can't be null")
    @JsonProperty("user_name")
    private String userName;

    @NotNull(message = "Employee email can't be null")
    @JsonProperty("user_email")
    private String userEmail;

    @NotNull(message = "Employee position can't be null")
    @JsonProperty("position")
    private String position;

    @NotNull(message = "Employee company name can't be null")
    @JsonProperty("company_name")
    private String companyName;

    @NotNull(message = "Employee department name can't be null")
    @JsonProperty("department_name")
    private String departmentName;

}