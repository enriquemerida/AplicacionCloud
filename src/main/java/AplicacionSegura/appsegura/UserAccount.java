package AplicacionSegura.appsegura;

import jakarta.persistence.*;

@Entity
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true, nullable=false, length=64)
    private String username;

    @Convert(converter = AesStringEncryptor.class)
    @Column(nullable=false)
    private String encSecret; // dato sensible cifrado (demo)

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public void setUsername(String u) { this.username = u; }
    public String getEncSecret() { return encSecret; }
    public void setEncSecret(String e) { this.encSecret = e; }
}
