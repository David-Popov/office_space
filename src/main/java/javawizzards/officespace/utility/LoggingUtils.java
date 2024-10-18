package javawizzards.officespace.utility;

public class LoggingUtils {

    public static String logMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static String logControllerName(Object controller) {
        return controller.getClass().getSimpleName();
    }
}
