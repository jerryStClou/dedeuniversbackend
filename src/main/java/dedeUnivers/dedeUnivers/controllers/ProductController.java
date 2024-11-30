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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/product")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    SubCategoryService subCategoryService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAll(){
        try{
            return new ResponseEntity<>(productService.getAllProduct(), HttpStatus.ACCEPTED);
        }catch (RuntimeException e){

        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/all/{idSubCategory}")
    public ResponseEntity<List<ProductProjection>> getAllProducts(@PathVariable int idSubCategory) {

        try {
            List<ProductProjection> products = productService.getProductsBySubCategoryId(idSubCategory);
            return new ResponseEntity<>(products, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//
//    @GetMapping("/all/wihtImage")
//    public ResponseEntity<Set<Product>> getAllProductsWithImage() {
//        try{
//            Set<Product> products = productService.getAllProductsWithImage();
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id")  Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                Product product = productService.findById(id);

                return new ResponseEntity<>(product, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/products/subcategory/{id}")
    public ResponseEntity<List<ProductProjection>> getProductsBySubCategoryId(@PathVariable Integer id) {
        try {
            List<ProductProjection> products = productService.getProductsBySubCategoryId(id);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/productProjection/{id}")
    public ResponseEntity<ProductProjection> getProductProjection(@PathVariable("id") Integer id) {
        try {
            ProductProjection product = productService.getProductProjectionById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//
//    @GetMapping("/products/subCategory/{name}")
//    public ResponseEntity<List<ProductProjection>> getProductsBySubCategory(@PathVariable("name") String name) {
//        try {
//            List<ProductProjection> products = productService.getProductsBySubCategoryName(name);
//            return new ResponseEntity<>(products, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @GetMapping("/products/subCategory/{id}/top10")
    public ResponseEntity<List<ProductProjection>> getTop10ProductsBySubCategory(@PathVariable("id") Integer id) {
        try {
            List<ProductProjection> products = productService.getTop10ProductsBySubCategoryId(id);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/products/by-subcategory/{nameSubCategory}")
    public ResponseEntity<List<ProductProjection>> getProductsBySubCategory(@PathVariable("nameSubCategory") String nameSubCategory) {
        try {
            List<ProductProjection> products = productService.getProductsByNameSubCateg(nameSubCategory);
            products = products ;
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            // Log erreur si nécessaire
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/add/{idSubCategory}")
    public ResponseEntity<Product> addProduct(@RequestBody Product product,@PathVariable Integer idSubCategory){
        try {
            if (product == null
                    || product.getNameProduct() == null || product.getNameProduct().trim().isEmpty()
                    || product.getDescription() == null || product.getDescription().trim().isEmpty()) {

                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            Product _product = productService.addProduct(product,idSubCategory);
            if (_product != null) {
                return new ResponseEntity<>(_product, HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @PutMapping("/update/{idProduct}")
    public ResponseEntity<String>updateProduct(@RequestBody Product product,@PathVariable("idProduct")  Integer idProduct){
        try {

            Product product1 = productService.findById(idProduct);
            product1.setNameProduct(product.getNameProduct());
            product1.setStock(product.getStock());
            product1.setDescription(product.getDescription());
            product1.setBasePrice(product.getBasePrice());
            product1.setBaseWeight(product.getBaseWeight());

            Product _product = productService.save(product1);
            if (_product != null) {
                return new ResponseEntity<String>("Le produit a pu etre modifier!", HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable("id") Integer id) {

        try {
            if (id <= 0) {
                return new ResponseEntity<String>("Erreur : L'id du produit à supprimer doit être > 0 !",
                        HttpStatus.BAD_REQUEST);
            }
            productService.remove(id);
            return new ResponseEntity<String>("Suppression du produit avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

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
    public ResponseEntity< List<Product>> getFourProduct(
            @PathVariable int subCategoryId) {
        try {

            List<Product> products = productService.getTop4ProductsBySubCategory(subCategoryId);
            return new ResponseEntity<>(products,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}

