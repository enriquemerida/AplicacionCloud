package AplicacionSegura.appsegura;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoginDto {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,32}$", message = "Usuario inválido")
    private String username;

    @NotBlank
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getPassword() { return password; }
    public void setPassword(String p) { this.password = p; }
}
