package javawizzards.officespace.enumerations.Util;

public enum Urls {
    PAYMENT_SUCCESS_REDIRECT("http://localhost:8087/swagger-ui/index.html"),
    PAYMENT_FAILURE_REDIRECT("http://localhost:8087/swagger-ui/index.html");

    private final String returnUrl;

    Urls(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getUrl() {
        return returnUrl;
    }
}
