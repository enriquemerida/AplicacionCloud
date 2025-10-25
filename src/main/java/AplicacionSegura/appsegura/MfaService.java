package AplicacionSegura.appsegura;

import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class MfaService {

    private final Map<String, String> codes = new HashMap<>();
    private final SecureRandom random = new SecureRandom();

    public String generateCode(String username) {
        String code = String.format("%06d", random.nextInt(999999));
        codes.put(username, code);
        System.out.println("✅ Código MFA para " + username + ": " + code);
        return code;
    }

    public boolean validateCode(String username, String code) {
        String stored = codes.get(username);
        return stored != null && stored.equals(code);
    }

    public void clearCode(String username) {
        codes.remove(username);
    }
}
