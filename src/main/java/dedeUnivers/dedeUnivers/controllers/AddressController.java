package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Address;
import dedeUnivers.dedeUnivers.services.AddressService;

import java.util.Set;
import java.util.regex.Pattern;

@RestController
@RequestMapping("api/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    // Méthode pour assainir les entrées utilisateurs
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remplace les caractères spéciaux par des entités HTML pour éviter l'exécution de code malicieux
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<Set<Address>> getAllAddressByUserId(@PathVariable Integer userId){
        try {
            Set<Address> addresses = addressService.getAllAddress(userId);
            return new ResponseEntity<>(addresses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getAddressById(@PathVariable Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                Address address = addressService.findById(id);
                return new ResponseEntity<>(address, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{idUser}")
    public ResponseEntity<Address> addAddress(@RequestBody Address address, @PathVariable("idUser") Integer idUser){
        try {
            // Sanitize the input fields before saving
            address.setCity(sanitizeInput(address.getCity()));
            address.setStreet(sanitizeInput(address.getStreet()));
            address.setPostalCode(sanitizeInput(address.getPostalCode()));

            Address address1 = addressService.addAddress(address, idUser);
            return new ResponseEntity<>(address1, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idAddress}")
    public ResponseEntity<String> updateAddress(@RequestBody Address address, @PathVariable Integer idAddress){
        try {
            Address address1 = addressService.findById(idAddress);

            // Sanitize the input fields before saving
            address1.setCity(sanitizeInput(address.getCity()));
            address1.setStreet(sanitizeInput(address.getStreet()));
            address1.setPostalCode(sanitizeInput(address.getPostalCode()));

            addressService.save(address1);
            return new ResponseEntity<>("L'adresse a pu être modifiée!", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeAddress(@PathVariable("id") Integer id) {
        try {
            addressService.deleteAddressById(id);
            return new ResponseEntity<>("Suppression de l'adresse avec id = '" + id + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
