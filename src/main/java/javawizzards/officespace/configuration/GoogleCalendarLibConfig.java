package javawizzards.officespace.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@NoArgsConstructor
public class GoogleCalendarLibConfig {

    @Value("${officeroom.lib.google.calendar.log.enabled:false}")
    private boolean logEnabled;

    @Value("${google.calendar.tokens-directory-path}")
    private String credentialsPath;

}