package dedeUnivers.dedeUnivers.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "adresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "city", nullable = false, length = 255)
    @Size(min = 2, max = 255)
    @NotNull
    private String city;

    @Column(name = "street", nullable = false, length = 255)
    @Size(min = 2, max = 255)
    @NotNull
    private String street;

    @Column(name = "postal_code", nullable = false, length = 5)
    @Size(min = 5, max = 5)
    @NotNull
    private String postalCode;




    // Constructors
    public Address() {}

    public Address(String city, String street, String postalCode) {
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
    }

}

