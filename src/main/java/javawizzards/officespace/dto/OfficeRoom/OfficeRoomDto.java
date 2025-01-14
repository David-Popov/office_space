package javawizzards.officespace.dto.OfficeRoom;

import jakarta.validation.constraints.PositiveOrZero;
import javawizzards.officespace.dto.Company.CompanyDto;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.dto.Resource.ResourceDto;
import javawizzards.officespace.enumerations.OfficeRoom.RoomStatus;
import javawizzards.officespace.enumerations.OfficeRoom.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OfficeRoomDto{
    @JsonProperty("id")
    private UUID id;

    @NotNull(message = "OfficeRoom name can't be null")
    @JsonProperty("officeRoomName")
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

    @JsonProperty("pictureUrl")
    private String pictureUrl;

    @NotNull
    @PositiveOrZero
    @JsonProperty("pricePerHour")
    private BigDecimal pricePerHour;

    @NotNull(message = "Company information can't be null")
    @JsonProperty("company")
    private CompanyDto company;

    @JsonProperty("reservations")
    private List<ReservationDto> reservations;

    @JsonProperty("resources")
    private List<ResourceDto> resources;
}
