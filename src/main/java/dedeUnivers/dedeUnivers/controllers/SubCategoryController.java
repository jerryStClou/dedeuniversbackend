package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.SubCategory;
import dedeUnivers.dedeUnivers.services.SubCategoryService;
import dedeUnivers.dedeUnivers.services.CategoryService;
import dedeUnivers.dedeUnivers.services.CategorySubCategoryService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/subCategory")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class SubCategoryController {

    @Autowired
    SubCategoryService subCategoryService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategorySubCategoryService categorySubCategoryService;

    // Méthode pour assainir les entrées utilisateur et éviter les attaques XSS
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remplacer les caractères spéciaux pour éviter les attaques XSS
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubCategory>> getAllSubCategories(@RequestParam(required = false) String nameSubCategory) {
        try {
            List<SubCategory> subCategories = new ArrayList<>();
            if (nameSubCategory == null || nameSubCategory.trim().isEmpty()) {
                subCategoryService.getAllSubCategory().forEach(subCategories::add);
            } else {
                subCategoryService.getByName(nameSubCategory).forEach(subCategories::add);
            }

            if (subCategories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(subCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategory> getSubCategoryById(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                SubCategory subCategory = subCategoryService.findById(id);
                return new ResponseEntity<>(subCategory, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("all/{idCategory}")
    public ResponseEntity<List<SubCategory>> getSubCategoriesByCategoryId(@PathVariable Integer idCategory) {
        try {
            List<SubCategory> subCategories = subCategoryService.getSubCategoriesByCategoryId(idCategory);
            return new ResponseEntity<>(subCategories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<SubCategory> addSubCategory(@RequestBody SubCategory subCategory, @RequestHeader("X-XSRF-TOKEN") String csrfToken) {
        System.out.println("Token CSRF reçu: " + csrfToken);  // Log du token CSRF reçu
        try {
            if (subCategory == null || subCategory.getNameSubCategory() == null || subCategory.getNameSubCategory().trim().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Assainir les champs avant d'enregistrer
            subCategory.setNameSubCategory(sanitizeInput(subCategory.getNameSubCategory()));
            subCategory.setImageSubCategory(sanitizeInput(subCategory.getImageSubCategory()));

            SubCategory _subCategory = subCategoryService.save(subCategory);
            if (_subCategory != null) {
                return new ResponseEntity<>(_subCategory, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<SubCategory> updateSubCategory(@RequestBody SubCategory subCategory, @PathVariable Integer id) {
        try {
            SubCategory subCategory1 = subCategoryService.findById(id);

            if (subCategory == null || subCategory.getNameSubCategory() == null || subCategory.getNameSubCategory().trim().isEmpty() || subCategory.getId() <= 0) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Assainir les champs avant de les mettre à jour
            subCategory1.setNameSubCategory(sanitizeInput(subCategory.getNameSubCategory()));
            subCategory1.setImageSubCategory(sanitizeInput(subCategory.getImageSubCategory()));

            SubCategory subCategory2 = subCategoryService.save(subCategory1);
            return new ResponseEntity<>(subCategory2, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeSubCategory(@PathVariable("id") Integer id) {
        try {
            if (id <= 0) {
                return new ResponseEntity<>("Erreur : L'id du produit à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }
            subCategoryService.remove(id);
            return new ResponseEntity<>("Suppression de la sous catégorie avec id = '" + id + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
