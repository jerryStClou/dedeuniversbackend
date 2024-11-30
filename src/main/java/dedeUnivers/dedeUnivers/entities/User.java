package dedeUnivers.dedeUnivers.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
// import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User  implements UserDetails  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lastname", nullable = false, length = 60)
    @Size(min = 2, max = 60)
    private String lastname;

    @Column(name = "firstname", nullable = false, length = 60)
    @Size(min = 2, max = 60)
    private String firstname;

    @Column(name = "pseudo", nullable = false, length = 60)
    @Size(min = 2, max = 60)
    private String pseudo;


    @Column(name = "image_profil", nullable = true)
    @Size(min = 0, max = 255)
    private String imageProfil;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 12, max = 40)
    private String password;


    private boolean actif = false;

    // @Column(name = "enabled")
    // private boolean enabled;

    @Column(name = "loyalty_points", nullable = false)
    private int loyaltyPoints = 0;  // Par défaut, l'utilisateur commence avec 0 points de fidélité

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Order> orders;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Comment> comments;




    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


    public User() {
    }

    public User(int id, String lastname, String firstname, String pseudo, String email, String password,String imageProfil
    ) {
        this.id = id;
        this.lastname = lastname;
        this.firstname = firstname;
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.imageProfil = imageProfil;
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getRoleType().getAuthorities();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.actif;
    }

    @Override
    public boolean isEnabled() {
        return this.actif;
    }

}
