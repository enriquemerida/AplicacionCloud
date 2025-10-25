package AplicacionSegura.appsegura;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
public class SecurityConfig {

    private final MfaEnforcementFilter mfaEnforcementFilter;
    private final LoginBlockFilter loginBlockFilter;

    public SecurityConfig(MfaEnforcementFilter mfaEnforcementFilter, LoginBlockFilter loginBlockFilter) {
        this.mfaEnforcementFilter = mfaEnforcementFilter;
        this.loginBlockFilter = loginBlockFilter;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .headers(h -> h
            .httpStrictTransportSecurity(hsts -> hsts.includeSubDomains(true).maxAgeInSeconds(31536000))
            .contentSecurityPolicy(csp -> csp.policyDirectives(
                "default-src 'self'; " +
                "script-src 'self'; style-src 'self' 'unsafe-inline'; " +
                "img-src 'self' data:; connect-src 'self'; frame-ancestors 'none'; base-uri 'self'"))
            .referrerPolicy(r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER))
            .permissionsPolicy(p -> p.policy("geolocation=(), microphone=(), camera=()"))
          )
          .authorizeHttpRequests(auth -> auth
            .requestMatchers("/login", "/mfa", "/echo", "/actuator/health", "/actuator/info").permitAll()
            .requestMatchers("/hello").hasAnyRole("ADMIN","USER")
            .anyRequest().authenticated()
          )
          .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/mfa", true)
            .permitAll()
          )
          .logout(logout -> logout
            .logoutSuccessUrl("/login?logout")
            .permitAll()
          )
          .sessionManagement(s -> s.maximumSessions(1))
          .httpBasic(b -> b.disable())
          .rememberMe(r -> r.disable());

        // Filtro que bloquea intentos si está rate-limited (antes de procesar login)
        http.addFilterAfter(loginBlockFilter, CsrfFilter.class);
        // Filtro que obliga a pasar MFA para cualquier recurso autenticado
        http.addFilterAfter(mfaEnforcementFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
