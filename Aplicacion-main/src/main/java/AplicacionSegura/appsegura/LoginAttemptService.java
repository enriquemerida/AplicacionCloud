package AplicacionSegura.appsegura;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginAttemptService {
    private static final int MAX_ATTEMPTS = 5;
    private static final long BLOCK_SECONDS = 600;

    private final Map<String, Integer> attempts = new ConcurrentHashMap<>();
    private final Map<String, Instant> blockedUntil = new ConcurrentHashMap<>();

    public boolean isBlocked(String key) {
        Instant until = blockedUntil.get(key);
        if (until == null) return false;
        if (Instant.now().isAfter(until)) {
            blockedUntil.remove(key);
            attempts.remove(key);
            return false;
        }
        return true;
    }

    public void loginFailed(String key) {
        int a = attempts.getOrDefault(key, 0) + 1;
        attempts.put(key, a);
        if (a >= MAX_ATTEMPTS) {
            blockedUntil.put(key, Instant.now().plusSeconds(BLOCK_SECONDS));
        }
    }

    public void loginSucceeded(String key) {
        attempts.remove(key);
        blockedUntil.remove(key);
    }
}
