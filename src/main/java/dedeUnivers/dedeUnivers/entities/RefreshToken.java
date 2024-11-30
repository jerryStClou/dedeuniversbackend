package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
//@Builder

@Setter
@Getter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "refreshToken", nullable = false, unique = true)
    private String refreshToken;

    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @Column(name = "expired", nullable = false)
    private boolean expired;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public RefreshToken() {
    }

    public RefreshToken(int id, String refreshToken, boolean disabled, boolean expired, LocalDateTime createdAt,
            LocalDateTime expiresAt,User user) {
        this.id = id;
        this.refreshToken = refreshToken;
        this.expired = expired;
        this.disabled = disabled;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.user = user;
    }

}
