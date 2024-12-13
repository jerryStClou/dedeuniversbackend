package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.services.ColorService;
import dedeUnivers.dedeUnivers.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/color")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @Autowired
    private ProductService productService;

    // Méthode pour assainir les entrées utilisateurs
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remplacer les caractères spéciaux pour éviter les attaques XSS
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }

    @GetMapping("all/{idProduct}")
    public ResponseEntity<List<Color>> getAllColorByIdProduct(@PathVariable("idProduct") Integer idProduct) {
        try{
            // Assainir l'ID du produit
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            List<Color> colors = colorService.getAllColorByProductId(Integer.parseInt(sanitizedIdProduct));
            return new ResponseEntity<>(colors, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable("id") Integer id) {
        try{
            // Assainir l'ID de la couleur
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                Color color = colorService.findById(Integer.parseInt(sanitizedId));
                return new ResponseEntity<>(color, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{idProduct}")
    public ResponseEntity<Color> addColor(@RequestBody Color color, @PathVariable Integer idProduct) {
        try {
            // Assainir les entrées avant de les utiliser
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            String sanitizedColor = sanitizeInput(color.getColor());

            // Créer la couleur avec l'ID et la couleur assainis
            Color _color = colorService.addColor(new Color(sanitizedColor), Integer.parseInt(sanitizedIdProduct));
            if (_color != null) {
                return new ResponseEntity<>(_color, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idColor}")
    public ResponseEntity<String> updateColor(@RequestBody Color color, @PathVariable Integer idColor) {
        try {
            // Assainir les entrées avant de les utiliser
            String sanitizedIdColor = sanitizeInput(idColor.toString());
            String sanitizedColor = sanitizeInput(color.getColor());

            // Mettre à jour la couleur avec l'ID et la couleur assainis
            Color color1 = colorService.findById(Integer.parseInt(sanitizedIdColor));
            color1.setColor(sanitizedColor);
            Color _color = colorService.save(color1);

            if (_color != null) {
                return new ResponseEntity<>("Le commentaire a pu être modifié!", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeColor(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID avant de l'utiliser
            String sanitizedId = sanitizeInput(id.toString());

            if (id <= 0) {
                return new ResponseEntity<>("Erreur : L'id du color à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }

            colorService.remove(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>("Suppression du color avec id = '" + sanitizedId + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
