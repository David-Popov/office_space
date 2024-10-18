package javawizzards.officespace.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class Response {
    private LocalDateTime date;
    private String errorDescription;
    private String ResponseID;
    private String ErrorCode;
    private String Description;

    public Response() {
        setDate(LocalDateTime.now());
        setResponseID(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS")));
        this.errorDescription = "";
        this.ErrorCode = "";
        this.Description = "";
    }

    public Response(String errorDescription, String errorCode, String description) {
        this.date = LocalDateTime.now();
        this.errorDescription = errorDescription;
        this.ErrorCode = errorCode;
        this.Description = description;
        setResponseID(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS")));
    }

    public Response(String errorDescription) {
        this.date = LocalDateTime.now();
        this.errorDescription = errorDescription;
        this.ErrorCode = "";
        this.Description = "";
        setResponseID(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSS")));
    }
}
