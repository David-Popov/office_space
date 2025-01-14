package javawizzards.officespace.enumerations.Util;

public enum Urls {
    PAYMENT_SUCCESS_REDIRECT("http://localhost:5173/payment-success"),
    PAYMENT_FAILURE_REDIRECT("http://localhost:5173/payment-failure");

    private final String returnUrl;

    Urls(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getUrl() {
        return returnUrl;
    }
}
