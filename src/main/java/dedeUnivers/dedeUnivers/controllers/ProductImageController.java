package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductImages;
import dedeUnivers.dedeUnivers.services.ProductImageService;
import dedeUnivers.dedeUnivers.services.ProductService;

import java.util.*;

@RestController
@RequestMapping("api/productImage")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ProductImageController {

    @Autowired
    ProductImageService productImageService;

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
    public ResponseEntity<List<ProductImages>> getAllCommentsByProductId(@PathVariable Integer idProduct) {
        try {
            // Assainir l'ID du produit
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            List<ProductImages> productImages = productImageService.getAllByProductId(Integer.parseInt(sanitizedIdProduct));
            if (productImages.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productImages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductImages> getProductImagesById(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID de l'image
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                ProductImages productImages = productImageService.findById(Integer.parseInt(sanitizedId));
                return new ResponseEntity<>(productImages, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/byIdsubCategory/{idSubCategory}")
    public ResponseEntity<Set<ProductImages>> getProductImagesByIdSubCategory(@PathVariable("idSubCategory") Integer idSubCategory) {
        try {
            // Assainir l'ID de la sous-catégorie
            String sanitizedIdSubCategory = sanitizeInput(idSubCategory.toString());
            Set<ProductImages> productImages = productImageService.getProductImagesByIdSubCategory(Integer.parseInt(sanitizedIdSubCategory));
            return new ResponseEntity<>(productImages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{idProduct}")
    public ResponseEntity<ProductImages> addProductImages(@RequestBody ProductImages productImages, @PathVariable Integer idProduct) {
        try {
            if (productImages == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Assainir les champs de l'image du produit
            String sanitizedProductImages = sanitizeInput(productImages.getProductImages());
            String sanitizedTypeProductImages = sanitizeInput(productImages.getTypeProductImages());

            productImages.setProductImages(sanitizedProductImages);
            productImages.setTypeProductImages(sanitizedTypeProductImages);

            Product product = productService.findById(idProduct);
            productImages.setProduct(product);
            ProductImages _productImages = productImageService.save(productImages);
            if (_productImages != null) {
                return new ResponseEntity<>(_productImages, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idProductImages}")
    public ResponseEntity<String> updateProductImages(@RequestBody ProductImages productImages, @PathVariable Integer idProductImages) {
        try {
            // Assainir les champs de l'image du produit
            String sanitizedProductImages = sanitizeInput(productImages.getProductImages());
            String sanitizedTypeProductImages = sanitizeInput(productImages.getTypeProductImages());

            ProductImages productImages1 = productImageService.findById(idProductImages);
            productImages1.setProductImages(sanitizedProductImages);
            productImages1.setTypeProductImages(sanitizedTypeProductImages);
            ProductImages _productImages = productImageService.save(productImages1);

            if (_productImages != null) {
                return new ResponseEntity<>("L'image du produit a pu être modifiée!", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeComment(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID de l'image du produit
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>("Erreur : L'id du Comment à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }
            productImageService.remove(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>("Suppression de l'image du produit avec id = '" + sanitizedId + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/product/{productId}/card")
    public ResponseEntity<List<ProductImages>> getProductImagesByProductIdAndType(@PathVariable int productId) {
        try {
            // Assainir l'ID du produit
            String sanitizedProductId = sanitizeInput(Integer.toString(productId));
            List<ProductImages> productImages = productImageService.getTop4ProductImagesByProductIdAndType(Integer.parseInt(sanitizedProductId));
            return new ResponseEntity<>(productImages, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
