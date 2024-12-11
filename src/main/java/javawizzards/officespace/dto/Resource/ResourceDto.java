package javawizzards.officespace.dto.Resource;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceDto {
    @NotNull(message = "Resource name can't be null")
    @JsonProperty("resource_name")
    private String name;

    @NotNull(message = "Resource name can't be null")
    @JsonProperty("resource_type")
    private String type;

    @NotNull(message = "Resource quantity can't be null")
    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("office_room_uuid")
    private UUID officeRoomId; 
}