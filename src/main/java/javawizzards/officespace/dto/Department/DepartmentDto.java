package javawizzards.officespace.dto.Department;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.enumerations.Department.DepartmentType;

import java.io.Serializable;

@Getter
@Setter
public class DepartmentDto implements Serializable {
    @NotNull(message = "Department name can't be null")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "Company name can't be null")
    @JsonProperty("company_name")
    private String companyName;

    @NotNull(message = "DepartmentType can't be null")
    @JsonProperty("departmentType")
    private DepartmentType departmentType;

}
