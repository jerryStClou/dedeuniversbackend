package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductImages;
import dedeUnivers.dedeUnivers.entities.SubCategory;
import dedeUnivers.dedeUnivers.projections.ProductProjection;
import dedeUnivers.dedeUnivers.services.ProductService;
import dedeUnivers.dedeUnivers.services.SubCategoryService;

import java.util.*;

@RestController
@RequestMapping("api/product")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    SubCategoryService subCategoryService;

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

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll(){
        try{
            return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.ACCEPTED);
        }catch (RuntimeException e){
            // Log erreur si nécessaire
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/all/{idSubCategory}")
    public ResponseEntity<List<ProductProjection>> getAllProducts(@PathVariable int idSubCategory) {
        try {
            // Assainir l'ID de la sous-catégorie
            String sanitizedIdSubCategory = sanitizeInput(Integer.toString(idSubCategory));
            List<ProductProjection> products = productService.getProductsBySubCategoryId(Integer.parseInt(sanitizedIdSubCategory));
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Integer id) {
        try{
            // Assainir l'ID du produit
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                Product product = productService.findById(Integer.parseInt(sanitizedId));
                return new ResponseEntity<>(product, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/products/subcategory/{id}")
    public ResponseEntity<List<ProductProjection>> getProductsBySubCategoryId(@PathVariable Integer id) {
        try {
            // Assainir l'ID de la sous-catégorie
            String sanitizedId = sanitizeInput(id.toString());
            List<ProductProjection> products = productService.getProductsBySubCategoryId(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{idSubCategory}")
    public ResponseEntity<Product> addProduct(@RequestBody Product product, @PathVariable Integer idSubCategory){
        try {
            // Assainir les entrées utilisateur
            String sanitizedProductName = sanitizeInput(product.getNameProduct());
            String sanitizedDescription = sanitizeInput(product.getDescription());

            if (product == null || sanitizedProductName.trim().isEmpty() || sanitizedDescription.trim().isEmpty()) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            product.setNameProduct(sanitizedProductName);
            product.setDescription(sanitizedDescription);

            // Assainir l'ID de la sous-catégorie
            String sanitizedIdSubCategory = sanitizeInput(idSubCategory.toString());
            Product _product = productService.addProduct(product, Integer.parseInt(sanitizedIdSubCategory));

            if (_product != null) {
                return new ResponseEntity<>(_product, HttpStatus.CREATED);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idProduct}")
    public ResponseEntity<String> updateProduct(@RequestBody Product product, @PathVariable("idProduct") Integer idProduct){
        try {
            // Assainir l'ID du produit
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());

            Product product1 = productService.findById(Integer.parseInt(sanitizedIdProduct));
            product1.setNameProduct(sanitizeInput(product.getNameProduct()));
            product1.setStock(product.getStock());
            product1.setDescription(sanitizeInput(product.getDescription()));
            product1.setBasePrice(product.getBasePrice());
            product1.setBaseWeight(product.getBaseWeight());

            Product _product = productService.save(product1);
            if (_product != null) {
                return new ResponseEntity<>("Le produit a pu être modifié!", HttpStatus.CREATED);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID du produit
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>("Erreur : L'id du produit à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }
            productService.remove(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>("Suppression du produit avec id = '" + sanitizedId + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/images/{subCategoryId}")
    public ResponseEntity<Map<SubCategory, List<List<ProductImages>>>> getGroupedProductImages(
            @PathVariable int subCategoryId) {

        Map<SubCategory, List<List<ProductImages>>> imagesGrouped = productService.getProductImagesGroupedBySubCategory(subCategoryId);

        return ResponseEntity.ok(imagesGrouped);
    }

    @GetMapping("/top4/{subCategoryId}")
    public ResponseEntity<List<Product>> getFourProduct(
            @PathVariable int subCategoryId) {
        try {
            // Assainir l'ID de la sous-catégorie
            String sanitizedSubCategoryId = sanitizeInput(Integer.toString(subCategoryId));
            List<Product> products = productService.getTop4ProductsBySubCategory(Integer.parseInt(sanitizedSubCategoryId));
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
