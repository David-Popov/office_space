package javawizzards.officespace.dto.Payment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SessionResponse {
    private String stripePaymentUrl;
    private String successRedirectUrl;
    private String failureRedirectUrl;
    private String sessionId;

    public SessionResponse(String stripePaymentUrl, String successRedirectUrl, String failureRedirectUrl, String sessionId) {
        this.stripePaymentUrl = stripePaymentUrl;
        this.successRedirectUrl = successRedirectUrl;
        this.failureRedirectUrl = failureRedirectUrl;
        this.sessionId = sessionId;
    }
}
