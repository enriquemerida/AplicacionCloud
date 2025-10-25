package AplicacionSegura.appsegura;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class LoginBlockFilter implements Filter {
    private final LoginAttemptService attempts;

    public LoginBlockFilter(LoginAttemptService attempts) {
        this.attempts = attempts;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest r = (HttpServletRequest) req;
        HttpServletResponse w = (HttpServletResponse) res;

        if ("/login".equals(r.getRequestURI()) && "POST".equalsIgnoreCase(r.getMethod())) {
            String key = r.getRemoteAddr();
            if (attempts.isBlocked(key)) {
                w.sendRedirect(r.getContextPath() + "/login?error=rate_limited");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
