package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.CategorySubCategory;
import dedeUnivers.dedeUnivers.services.CategorySubCategoryService;

import java.util.List;

@RestController
@RequestMapping("api/category-subCategory")
public class CategorySubCategoryController {

    @Autowired
    private CategorySubCategoryService categorySubCategoryService;

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

    @GetMapping("/all")
    public ResponseEntity<List<CategorySubCategory>> getAll() {
        try {
            List<CategorySubCategory> categorySubCategories = categorySubCategoryService.getAll();
            return new ResponseEntity<>(categorySubCategories, HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/add/{idCategory}/{idSubCategory}")
    public ResponseEntity<String> addCategorySubCategory(@PathVariable("idCategory") Integer idCategory,
                                                         @PathVariable("idSubCategory") Integer idSubCategory) {
        try {
            // Assainir les paramètres avant de les utiliser
            String sanitizedIdCategory = sanitizeInput(idCategory.toString());
            String sanitizedIdSubCategory = sanitizeInput(idSubCategory.toString());

            // Appel à la méthode service avec les paramètres assainis
            CategorySubCategory categorySubCategory = categorySubCategoryService.addCategorySubCategory(
                    Integer.parseInt(sanitizedIdCategory), Integer.parseInt(sanitizedIdSubCategory)
            );

            return new ResponseEntity<>("Le lien a été créé", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idCategorySubCategory}/{idCategory}/{idSubCategory}")
    public ResponseEntity<String> updateCategorySubCategory(@PathVariable("idCategorySubCategory") Integer idCategorySubCategory,
                                                            @PathVariable("idCategory") Integer idCategory,
                                                            @PathVariable("idSubCategory") Integer idSubCategory) {
        try {
            // Assainir les paramètres avant de les utiliser
            String sanitizedIdCategorySubCategory = sanitizeInput(idCategorySubCategory.toString());
            String sanitizedIdCategory = sanitizeInput(idCategory.toString());
            String sanitizedIdSubCategory = sanitizeInput(idSubCategory.toString());

            // Appel à la méthode service avec les paramètres assainis
            CategorySubCategory categorySubCategory = categorySubCategoryService.updateCategorySubCategory(
                    Integer.parseInt(sanitizedIdCategorySubCategory),
                    Integer.parseInt(sanitizedIdCategory),
                    Integer.parseInt(sanitizedIdSubCategory)
            );

            return new ResponseEntity<>("Le lien a été modifié !", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Promotion non trouvée
        }
    }

    @DeleteMapping("/remove/{idCategorySubCategory}")
    public ResponseEntity<String> deleteCategorySubCategory(@PathVariable("idCategorySubCategory") Integer idCategorySubCategory) {
        try {
            // Assainir l'ID avant de l'utiliser
            String sanitizedIdCategorySubCategory = sanitizeInput(idCategorySubCategory.toString());

            // Appel à la méthode service avec l'ID assainit
            categorySubCategoryService.deleteCategorySubCategory(Integer.parseInt(sanitizedIdCategorySubCategory));

            return new ResponseEntity<>("Suppression du lien entre catégorie et sous-catégorie avec id = '" + sanitizedIdCategorySubCategory
                    + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
