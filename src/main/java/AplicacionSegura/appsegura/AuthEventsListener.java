package AplicacionSegura.appsegura;

import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class AuthEventsListener {
    private final LoginAttemptService attemptService;

    public AuthEventsListener(LoginAttemptService s) {
        this.attemptService = s;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent e) {
        String key = clientIp();
        attemptService.loginSucceeded(key);
        System.out.println("LOGIN OK from " + key);
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent e) {
        String key = clientIp();
        attemptService.loginFailed(key);
        System.out.println("LOGIN FAIL from " + key);
    }

    private String clientIp() {
        var attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) return "unknown";
        HttpServletRequest req = attrs.getRequest();
        return req != null ? req.getRemoteAddr() : "unknown";
    }
}
