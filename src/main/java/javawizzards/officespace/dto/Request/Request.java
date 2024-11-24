package javawizzards.officespace.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;


@NoArgsConstructor
@Getter
@Setter
public class Request<T> implements Serializable {
    private String requestId;
    private LocalDateTime timestamp;

    @JsonProperty("data")
    private T data;
}
