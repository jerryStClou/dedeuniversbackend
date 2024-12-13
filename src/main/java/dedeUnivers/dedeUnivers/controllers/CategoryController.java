package dedeUnivers.dedeUnivers.controllers;

import dedeUnivers.dedeUnivers.entities.Category;
import dedeUnivers.dedeUnivers.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/category")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    // Méthode pour échapper les caractères spéciaux afin d'éviter les attaques XSS
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }

        // Remplacer les caractères spéciaux par leurs équivalents HTML
        return input.replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Category>> getAllCategories(@RequestParam(required = false) String nameCategory) {
        try {
            List<Category> categories = new ArrayList<>();
            if (nameCategory == null || nameCategory.trim().isEmpty()) {
                categoryService.getAllCategory().forEach(categories::add);
            } else {
                // Sanitize the search input to prevent XSS
                nameCategory = sanitizeInput(nameCategory);
                categoryService.getByName(nameCategory).forEach(categories::add);
            }
            if (categories.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer id) {
        try {
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                Category category = categoryService.findById(id);
                return new ResponseEntity<>(category, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        try {
            if (category == null
                    || category.getNameCategory() == null || category.getNameCategory().trim().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Sanitize the category name before saving to avoid XSS
            category.setNameCategory(sanitizeInput(category.getNameCategory()));

            Category _category = categoryService.save(category);
            if (_category != null) {
                return new ResponseEntity<>(_category, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idCategory}")
    public ResponseEntity<String> updateCategory(@RequestBody Category category, @PathVariable Integer idCategory) {
        try {
            category.setId(idCategory);
            // Sanitize the category name before saving to avoid XSS
            category.setNameCategory(sanitizeInput(category.getNameCategory()));

            Category _category = categoryService.save(category);
            if (_category != null) {
                return new ResponseEntity<>("La catégorie a pu être modifiée !", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeCategory(@PathVariable("id") Integer id) {
        try {
            if (id <= 0) {
                return new ResponseEntity<>("Erreur : L'id de la catégorie à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }
            categoryService.remove(id);
            return new ResponseEntity<>("Suppression de la catégorie avec id = '" + id + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

