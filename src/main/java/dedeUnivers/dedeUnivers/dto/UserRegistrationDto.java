package dedeUnivers.dedeUnivers.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import dedeUnivers.dedeUnivers.entities.Role;
import lombok.Getter;

@Getter
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
    @Size(min = 12, max = 255)
    private String password;

    @NotBlank
    @Size(min = 12, max = 255)
    private String confirmPassword;

    // Constructor, Getters and Setters
    
    public UserRegistrationDto() {
    }

    public UserRegistrationDto(String lastname, String firstname, String pseudo, String imageProfil, String email, String password, String confirmPassword) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.pseudo = pseudo;
        this.imageProfil = imageProfil;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
// Getters and Setters
//
//public String getLastname() {
//    return lastname;
//}
//
//public String getFirstname() {
//    return firstname;
//}
//
//public String getPseudo() {
//    return pseudo;
//}
//
//    public String getImageProfil() {
//        return imageProfil;
//    }
//
//public String getEmail() {
//    return email;
//}
//
//public String getPassword() {
//    return password;
//}

}
