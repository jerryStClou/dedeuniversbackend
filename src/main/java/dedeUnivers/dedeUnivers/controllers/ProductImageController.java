package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductImages;
import dedeUnivers.dedeUnivers.services.ProductImageService;
import dedeUnivers.dedeUnivers.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/productImage")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ProductImageController {

    @Autowired
    ProductImageService productImageService;

    @Autowired
    ProductService productService;



    @GetMapping("/all/{idProduct}")
    public ResponseEntity<List<ProductImages>> getAllCommentsByProductId(@PathVariable Integer idProduct) {

        try {
            List<ProductImages> productImages = productImageService.getAllByProductId(idProduct);
            if (productImages.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productImages, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<ProductImages> getProductImagesById(@PathVariable("id")  Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                ProductImages productImages = productImageService.findById(id);
                return new ResponseEntity<>(productImages, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/byIdsubCategory/{idSubCategory}")
    public ResponseEntity<Set<ProductImages>> getProductImagesByIdSubCategory(@PathVariable("idSubCategory")  Integer idSubCategory){
        try{
            Set<ProductImages> productImages = productImageService.getProductImagesByIdSubCategory(idSubCategory);
            return new ResponseEntity<>(productImages, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PostMapping("/add/{idProduct}")
    public ResponseEntity<ProductImages> addProductImages(@RequestBody ProductImages productImages,@PathVariable Integer idProduct){
        try {
            if (productImages == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Product product = productService.findById(idProduct);
            productImages.setProduct(product);
            ProductImages _productImages = productImageService.save(productImages);
            if (_productImages != null) {
                return new ResponseEntity<>(_productImages, HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @PutMapping("/update/{idProductImages}")
    public ResponseEntity<String>updateProductImages(@RequestBody ProductImages productImages,@PathVariable Integer idProductImages){
        try {
            ProductImages productImages1 = productImageService.findById(idProductImages);
            productImages1.setProductImages(productImages.getProductImages());
            productImages1.setTypeProductImages(productImages.getTypeProductImages());
            ProductImages _productImages = productImageService.save(productImages1);

            if (_productImages != null) {
                return new ResponseEntity<String>("L'image du produit à pu être modifier!", HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeComment(@PathVariable("id") Integer id) {

        try {
            if (id <= 0) {
                return new ResponseEntity<String>("Erreur : L'id du Comment à supprimer doit être > 0 !",
                        HttpStatus.BAD_REQUEST);
            }
            productImageService.remove(id);
            return new ResponseEntity<String>("Suppression du product Images avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/product/{productId}/card")
    public ResponseEntity<List<ProductImages>> getProductImagesByProductIdAndType(
            @PathVariable int productId) {
        try {
            List<ProductImages> productImages = productImageService.getTop4ProductImagesByProductIdAndType(productId);
            return new ResponseEntity<>(productImages,HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}

