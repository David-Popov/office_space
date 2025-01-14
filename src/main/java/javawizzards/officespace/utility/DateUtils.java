package javawizzards.officespace.utility;

import com.google.api.client.util.DateTime;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtils {

    public static DateTime convertLocalDateTimeToDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'-03:00'");
        String date = dateTime.format(formatter);
        return new DateTime(date);
    }
}