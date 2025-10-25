package AplicacionSegura.appsegura;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class BackupService {
    private final UserAccountRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    public BackupService(UserAccountRepository repo) { this.repo = repo; }

    // Backup cada hora (demo): exporta a JSON
    @Scheduled(cron = "0 0 * * * *")
    public void backup() {
        try {
            List<UserAccount> all = repo.findAll();
            mapper.writeValue(new File("backup-useraccounts.json"), all);
            System.out.println("Backup OK");
        } catch (Exception e) {
            System.err.println("Backup FAIL: " + e.getMessage());
        }
    }

    // "Restore dry-run" diario (demo)
    @Scheduled(cron = "0 30 2 * * *")
    public void restoreDryRun() {
        try {
            mapper.readValue(new File("backup-useraccounts.json"),
                mapper.getTypeFactory().constructCollectionType(List.class, UserAccount.class));
            System.out.println("Restore DRY-RUN OK");
        } catch (Exception e) {
            System.err.println("Restore DRY-RUN FAIL: " + e.getMessage());
        }
    }
}
