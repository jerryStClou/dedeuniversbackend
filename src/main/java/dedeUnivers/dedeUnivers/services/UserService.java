package dedeUnivers.dedeUnivers.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import dedeUnivers.dedeUnivers.entities.Role;
import dedeUnivers.dedeUnivers.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dedeUnivers.dedeUnivers.dto.UserRegistrationDto;
import dedeUnivers.dedeUnivers.entities.User;
import dedeUnivers.dedeUnivers.entities.Validation;
import dedeUnivers.dedeUnivers.projections.ValidationCodeProjection;
import dedeUnivers.dedeUnivers.repositories.UserRepository;
import dedeUnivers.dedeUnivers.repositories.ValidationRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private ValidationRepository validationRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;  // Assurez-vous d'injecter le repository du rôle



    public User findById(Integer id){
        return userRepository.findById(id).get();
    }


    public String generateValidationCode() {
        int length = 12;
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz$!:;,0123456789";
        Random random = new Random();
        StringBuilder validationCode = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            validationCode.append(characters.charAt(random.nextInt(characters.length())));
        }
        return validationCode.toString();
    }


    
    public void sendValidationEmail(String toEmail, String validationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Validation Code");
        message.setText("Your validation code is: " + validationCode);
        mailSender.send(message);
    }

    
    public void saveValidationCode(String code,String email) {
        Validation validationCode = new Validation();
        validationCode.setValidationCode(code);
        validationCode.setEmail(email);
        validationCode.setValidationCodeExpiry(LocalDateTime.now().plusMinutes(5));
        validationRepository.save(validationCode);
    }

    public Validation saveValidation (Validation validation){
        return validationRepository.save(validation);
    }


    public Optional<Validation> getValidationByCode (String ValidationCode){
        return validationRepository.findByValidationCode(ValidationCode);
    }

    public String register(UserRegistrationDto registrationDto) {
        // Vérifier si l'email est déjà validé
        Optional<Validation> vOptional = validationRepository.findByEmail(registrationDto.getEmail());
        if (vOptional.isEmpty()) {
            throw new IllegalArgumentException("Aucune validation trouvée pour cet email");
        }

        Validation validation = vOptional.get();
        if (!validation.getActivation()) {
            throw new IllegalArgumentException("L'activation n'est pas encore effectuée");
        }

        // Créer un utilisateur
        User user = new User();
        user.setLastname(registrationDto.getLastname());
        user.setFirstname(registrationDto.getFirstname());
        user.setPseudo(registrationDto.getPseudo());
        user.setImageProfil(registrationDto.getImageProfil());
        user.setEmail(registrationDto.getEmail());

        // Encoder le mot de passe
        String passwordEncrypt = passwordEncoder.encode(registrationDto.getPassword());
        user.setPassword(passwordEncrypt);

        // Récupérer le rôle
        Optional<Role> roleOptional = roleRepository.findById(2);
        if (roleOptional.isEmpty()) {
            throw new IllegalArgumentException("Rôle non trouvé pour l'ID spécifié");
        }
        user.setRole(roleOptional.get());

        // Sauvegarder l'utilisateur
        validation.setUser(user);
        userRepository.save(user);
        return "L'utilisateur a bien été enregistrer dans la base de données";
    }



    public Optional<User> getById(Integer userId){
        return userRepository.findById(userId);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Recherche de l'utilisateur par email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Retourne un UserDetails de Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities() // Assuming the User class provides authorities (roles)
        );
    }



    
    // Méthode pour générer un nouveau mot de passe
//    public void newPassword(String email) {
//        try {
//            String newPassword = generateValidationCode(); // Réutilisation de la méthode pour générer un mot de passe
//            String encodedPassword = passwordEncoder.encode(newPassword);
//
//            // Mettre à jour le mot de passe de l'utilisateur dans la base de données
//            Optional<User> optionalUser = userRepository.findByEmail(email);
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                user.setPassword(encodedPassword);
//                userRepository.save(user);
//
//                // Envoyer le nouveau mot de passe à l'utilisateur par email
//                SimpleMailMessage message = new SimpleMailMessage();
//                message.setTo(email);
//                message.setSubject("Nouveau mot de passe");
//                message.setText("Votre nouveau mot de passe est: " + newPassword);
//                mailSender.send(message);
//            }
//        } catch (UsernameNotFoundException e) {
//            throw new RuntimeException("User not found with email: " + email);
//        }
//    }


    public String newPassword(String email) {
        try {
            String newPassword = generateValidationCode(); // Réutilisation de la méthode pour générer un mot de passe
            String encodedPassword = passwordEncoder.encode(newPassword);
            return newPassword;
            // Mettre à jour le mot de passe de l'utilisateur dans la base de données
            //Optional<User> optionalUser = userRepository.findByEmail(email);
//            if (optionalUser.isPresent()) {
//                User user = optionalUser.get();
//                user.setPassword(encodedPassword);
//                userRepository.save(user);

                // Envoyer le nouveau mot de passe à l'utilisateur par email

            //}
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("User not found with email: " + email);
        }
    }


    
    // Méthode pour changer le mot de passe de l'utilisateur
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        try {
            UserDetails userDetails = loadUserByUsername(email);
            if (passwordEncoder.matches(oldPassword, userDetails.getPassword())) {
                String encodedNewPassword = passwordEncoder.encode(newPassword);

                // Mettre à jour le mot de passe de l'utilisateur dans la base de données
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                    User user = optionalUser.get();
                    user.setPassword(encodedNewPassword);
                    userRepository.save(user);
                    return true;
                }
            } else {
                return false; // L'ancien mot de passe ne correspond pas
            }
        } catch (UsernameNotFoundException e) {
            throw new RuntimeException("User not found with email: " + email);
        }
        return false;
    }


    // Méthode pour vérifier le mot de passe
    public boolean checkPassword(String rawPassword, String storedPassword) {
        // Utilisez l'instance de PasswordEncoder pour appeler matches
        return passwordEncoder.matches(rawPassword, storedPassword);
    }



    public Optional<User> getUserByEmail(String email) {
        // Utilise le repository pour trouver un utilisateur par email
        return userRepository.findByEmail(email);
    }

}
