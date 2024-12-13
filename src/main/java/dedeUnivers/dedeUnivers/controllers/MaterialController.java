package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Material;
import dedeUnivers.dedeUnivers.services.MaterialService;
import dedeUnivers.dedeUnivers.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/material")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @Autowired
    ProductService productService;

    // Méthode pour assainir les entrées utilisateur
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remplacer les caractères spéciaux pour éviter les attaques XSS
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }

    @GetMapping("/all/{idProduct}")
    public ResponseEntity<List<Material>> getAllMaterialsByProductId(@PathVariable Integer idProduct) {
        try {
            // Assainir l'ID du produit
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            List<Material> materials = materialService.getAllByProductId(Integer.parseInt(sanitizedIdProduct));
            if (materials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(materials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID du matériau
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Material material = materialService.findById(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>(material, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{idProduct}")
    public ResponseEntity<Material> addMaterial(@RequestBody Material material, @PathVariable Integer idProduct) {
        try {
            if (material == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            // Assainir les données avant de les enregistrer
            String sanitizedMaterial = sanitizeInput(material.getMaterial());
            material.setMaterial(sanitizedMaterial);

            // Assainir les valeurs de influenceMaterialWeight et influenceMaterialPrice en float
            String sanitizedInfluenceWeight = sanitizeInput(Float.toString(material.getInfluenceMaterialWeight()));
            material.setInfluenceMaterialWeight(Float.parseFloat(sanitizedInfluenceWeight));

            String sanitizedInfluencePrice = sanitizeInput(Float.toString(material.getInfluenceMaterialPrice()));
            material.setInfluenceMaterialPrice(Float.parseFloat(sanitizedInfluencePrice));

            String sanitizedProductId = sanitizeInput(idProduct.toString());
            Material _material = materialService.addMaterial(material, Integer.parseInt(sanitizedProductId));

            if (_material != null) {
                return new ResponseEntity<>(_material, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idMaterial}")
    public ResponseEntity<String> updateMaterial(@RequestBody Material material, @PathVariable Integer idMaterial) {
        try {
            // Assainir l'ID du matériau
            String sanitizedIdMaterial = sanitizeInput(idMaterial.toString());
            String sanitizedMaterial = sanitizeInput(material.getMaterial());
            String sanitizedInfluenceWeight = sanitizeInput(Float.toString(material.getInfluenceMaterialWeight()));
            String sanitizedInfluencePrice = sanitizeInput(Float.toString(material.getInfluenceMaterialPrice()));

            Material material1 = materialService.findById(Integer.parseInt(sanitizedIdMaterial));
            material1.setMaterial(sanitizedMaterial);
            material1.setInfluenceMaterialWeight(Float.parseFloat(sanitizedInfluenceWeight));  // Utilisation de float
            material1.setInfluenceMaterialPrice(Float.parseFloat(sanitizedInfluencePrice));  // Utilisation de float

            Material _material = materialService.save(material1);
            if (_material != null) {
                return new ResponseEntity<>("Le matériau a pu être modifié!", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeMaterial(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID avant de l'utiliser
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>("Erreur : L'id du Material à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }
            materialService.remove(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>("Suppression du material avec id = '" + sanitizedId + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
