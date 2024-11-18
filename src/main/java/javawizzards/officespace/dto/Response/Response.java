package javawizzards.officespace.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Response<T> implements Serializable {
    private LocalDateTime date;
    private String errorDescription;
    private String responseId;
    private HttpStatus status;
    private String description;
    private T data;

    public Response() {
        this.date = LocalDateTime.now();
        this.responseId = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS"));
        this.errorDescription = "";
        this.status = HttpStatus.OK;
        this.description = "";
        this.data = null;
    }

    public Response(T data, HttpStatus status, String description) {
        this();
        this.data = data;
        this.status = status;
        this.description = description;
        this.errorDescription = "";
    }

    public Response(HttpStatus status, String description) {
        this();
        this.data = null;
        this.status = status;
        this.description = description;
        this.errorDescription = "";
    }

    public Response(String errorDescription, HttpStatus status, String description) {
        this();
        this.errorDescription = errorDescription;
        this.status = status;
        this.description = description;
        this.data = null;
    }

    public Response(String errorDescription) {
        this();
        this.errorDescription = errorDescription;
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
        this.description = "";
        this.data = null;
    }
}
