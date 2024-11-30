package dedeUnivers.dedeUnivers.entities;
import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "validations")
public class Validation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
 
    @Column(name = "validation_code", nullable = false)
    @Size(min = 0, max = 100)
    private String validationCode;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    private String email;
    
    @Column(name = "localDateTime")
    private LocalDateTime validationCodeExpiry;

    @Column(name = "activation")
    private boolean activation = false;

    @OneToOne(cascade={CascadeType.MERGE, CascadeType.DETACH})
    private User user;


    public Validation() {
    }

    public Validation(int id, String validationCode, LocalDateTime validationCodeExpiry,String email,boolean activation) {
        this.id = id;
        this.validationCode = validationCode;
        this.validationCodeExpiry = validationCodeExpiry;
        this.email = email;
        this.activation = activation;
    }
    
    public boolean getActivation() {
        return activation;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }
    
}
