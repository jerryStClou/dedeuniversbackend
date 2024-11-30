package dedeUnivers.dedeUnivers.entities;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "jwt")
public class Jwt {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    @JoinColumn(name = "refresh_token_id")  // Le nom de la colonne pour la clé étrangère
    private RefreshToken refreshToken;

    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE })
    @JoinColumn(name = "user_id")
    private User user;

    public Jwt() {
    }

    public Jwt(String token, boolean disabled, boolean expired, LocalDateTime createdAt, LocalDateTime expiresAt,
            User user) {
        this.token = token;
        this.expired = expired;
        this.disabled = disabled;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
