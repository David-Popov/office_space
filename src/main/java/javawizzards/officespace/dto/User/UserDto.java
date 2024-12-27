package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.dto.Notification.SendNotificationDto;
import javawizzards.officespace.dto.Notification.UserNotificationDto;
import javawizzards.officespace.dto.Payment.PaymentResponse;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    @JsonProperty("Id")
    private UUID id;

    @NotNull(message = "Email can't be null")
    @Email(message = "Email is invalid")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Username can't be null")
    @JsonProperty("username")
    private String username;

    @NotNull(message = "FirstName can't be null")
    @JsonProperty("firstName")
    private String firstName;

    @NotNull(message = "LastName can't be null")
    @JsonProperty("lastName")
    private String lastName;

    @NotNull(message = "Phone can't be null")
    @JsonProperty("phone")
    private String phone;

    @NotNull
    @JsonProperty("roleId")
    private int roleId;

    @NotNull
    @JsonProperty("roleName")
    private String roleName;

    @NotNull
    @JsonProperty("reservations")
    private List<ReservationDto> reservations;

    @NotNull
    @JsonProperty("payments")
    private List<PaymentResponse> payments;

    @NotNull
    @JsonProperty("notifications")
    private List<UserNotificationDto> notifications;
}
