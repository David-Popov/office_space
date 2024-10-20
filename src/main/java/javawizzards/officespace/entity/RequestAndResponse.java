package javawizzards.officespace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "RequestsAndResponses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestAndResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    @NotNull(message = "Date cannot be null")
    private LocalDateTime date;

    @Column(nullable = false)
    @NotEmpty(message = "Controller name cannot be empty")
    private String controllerName;

    @Column(nullable = false)
    @NotEmpty(message = "Method name cannot be empty")
    private String methodName;

    @Column(nullable = false)
    @NotEmpty(message = "Request ID cannot be empty")
    private String requestId;

    @Column(nullable = false, length = 500)
    @NotEmpty(message = "Request data cannot be empty")
    private String requestData;

    @Column(nullable = false)
    @NotEmpty(message = "Response ID cannot be empty")
    private String responseId;

    @Column(nullable = true, length = 500)
    private String responseData;

    @Column(nullable = true)
    private String errorMessage;
}
