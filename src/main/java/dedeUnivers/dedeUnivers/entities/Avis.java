package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "avis")
public class Avis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String avis;
    private String titleAvis;

    @ManyToOne
    private User user; // 

    
    public Avis() {
    }

    
    public Avis(int id,String avis, String titleAvis) {
        this.id = id;
        this.avis = avis;
        this.titleAvis = titleAvis;
    }

}
