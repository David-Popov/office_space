package javawizzards.officespace.utility;

import org.springframework.stereotype.Component;

public class LoggingUtils {

    public static String logMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static String logControllerName(Object controller) {
        return controller.getClass().getSimpleName();
    }
}
