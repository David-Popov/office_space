package javawizzards.officespace.dto.Resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import javawizzards.officespace.enumerations.Resource.ResourceStatus;
import javawizzards.officespace.enumerations.Resource.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDto {

    @NotBlank(message = "Resource name cannot be blank")
    @JsonProperty("resource_name")
    private String name;

    @NotNull(message = "Resource type must be specified")
    @JsonProperty("resource_type")
    private ResourceType type;

    @NotNull(message = "Resource quantity must be specified")
    @PositiveOrZero(message = "Quantity must be zero or positive")
    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("description")
    private String description;

    @NotNull(message = "Resource status must be specified")
    @JsonProperty("status")
    private ResourceStatus status = ResourceStatus.AVAILABLE;

    @JsonProperty("maintenance_notes")
    private String maintenanceNotes;

    @JsonProperty("last_maintenance_date")
    private String lastMaintenanceDate;
}