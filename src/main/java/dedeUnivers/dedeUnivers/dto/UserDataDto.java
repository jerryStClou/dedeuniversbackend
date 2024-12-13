package dedeUnivers.dedeUnivers.dto;

public class UserDataDto {
    private int id;
    private String firstname;
    private String lastname;
    private String pseudo;
    private String imageProfil;
    private String email;
    private int loyaltyPoints;
    private String role;

    // Constructor, getters, and setters


    public UserDataDto(int id, String firstname, String lastname, String pseudo, String imageProfil, String email, int loyaltyPoints, String role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.pseudo = pseudo;
        this.imageProfil = imageProfil;
        this.email = email;
        this.loyaltyPoints = loyaltyPoints;
        this.role = role;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getImageProfil() {
        return imageProfil;
    }

    public void setImageProfil(String imageProfil) {
        this.imageProfil = imageProfil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

