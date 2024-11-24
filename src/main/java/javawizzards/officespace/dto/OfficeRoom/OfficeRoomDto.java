package javawizzards.officespace.dto.OfficeRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OfficeRoomDto{
    @NotNull(message = "OfficeRoom name can't be null")
    @JsonProperty("office_room_name")
    private String officeRoomName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("building")
    private String building;

    @NotNull(message = "OfficeRoom floor can't be null")
    @JsonProperty("floor")
    private String floor;

    @JsonProperty("type")
    private String type;

    @NotNull(message = "OfficeRoom capacity can't be null")
    @JsonProperty("capacity")
    private Integer capacity;

    @NotNull(message = "OfficeRoom status can't be null")
    @JsonProperty("status")
    private String status;

    @JsonProperty("pictureUrl")
    private String pictureUrl;

    @NotNull(message = "OfficeRoom company name can't be null")
    @JsonProperty("company_uuid")
    private UUID companyId;

}
