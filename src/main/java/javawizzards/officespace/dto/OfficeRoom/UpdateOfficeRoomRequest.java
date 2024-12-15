package javawizzards.officespace.dto.OfficeRoom;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import javawizzards.officespace.enumerations.OfficeRoom.RoomStatus;
import javawizzards.officespace.enumerations.OfficeRoom.RoomType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class UpdateOfficeRoomRequest {
    @JsonProperty("id")
    private UUID id;

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

    @NotNull(message = "Room type is required")
    @JsonProperty("type")
    private RoomType type;

    @NotNull(message = "OfficeRoom capacity can't be null")
    @JsonProperty("capacity")
    private int capacity;

    @NotNull(message = "OfficeRoom status can't be null")
    @JsonProperty("status")
    private RoomStatus status;

    @JsonProperty("picture_url")
    private String pictureUrl;

    @NotNull
    @PositiveOrZero
    @JsonProperty("price_per_hour")
    private BigDecimal pricePerHour;

    @NotNull(message = "Company Id can't be null")
    @JsonProperty("company")
    private UUID companyId;
}
