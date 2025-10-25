package AplicacionSegura.appsegura;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Set;

@Component
public class MfaEnforcementFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWLIST = Set.of(
        "/login", "/mfa", "/error", "/css/", "/js/", "/images/", "/actuator/health", "/actuator/info"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        boolean allowlisted = ALLOWLIST.stream().anyMatch(path::startsWith);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !allowlisted) {
            HttpSession session = req.getSession(false);
            Boolean mfaOk = (session != null) ? (Boolean) session.getAttribute("MFA_OK") : null;
            if (mfaOk == null || !mfaOk) {
                res.sendRedirect(req.getContextPath() + "/mfa");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
