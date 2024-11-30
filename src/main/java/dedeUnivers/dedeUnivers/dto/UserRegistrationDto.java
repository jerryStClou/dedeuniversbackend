package dedeUnivers.dedeUnivers.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import dedeUnivers.dedeUnivers.entities.Role;

public class UserRegistrationDto {

   
    @NotBlank
    @Size(min = 2, max = 60)
    private String lastname;

    @NotBlank
    @Size(min = 2, max = 60)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 60)
    private String pseudo;

    @NotBlank
    @Size(min = 0, max = 255)
    private String imageProfil;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 12, max = 40)
    private String password;

//
//    @Size(min = 0, max = 100)
//    private String validationCode;
//
//    private LocalDateTime validationCodeExpiry;
//

    // Constructor, Getters and Setters
    
    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String lastname, String firstname, String pseudo, String email, String password,String imageProfil) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.pseudo = pseudo;
        this.email = email;
        this.password = password;
        this.imageProfil = imageProfil;
    }
// Getters and Setters

public String getLastname() {
    return lastname;
}

public void setLastname(String lastname) {
    this.lastname = lastname;
}

public String getFirstname() {
    return firstname;
}

public void setFirstname(String firstname) {
    this.firstname = firstname;
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

public String getPassword() {
    return password;
}

public void setPassword(String password) {
    this.password = password;
}

//public String getValidationCode() {
//    return validationCode;
//}
//
//public void setValidationCode(String validationCode) {
//    this.validationCode = validationCode;
//}
//
//public LocalDateTime getValidationCodeExpiry() {
//    return validationCodeExpiry;
//}
//
//public void setValidationCodeExpiry(LocalDateTime validationCodeExpiry) {
//    this.validationCodeExpiry = validationCodeExpiry;
//}
//

}
