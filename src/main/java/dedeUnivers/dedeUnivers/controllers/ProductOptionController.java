package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.services.ColorService;
import dedeUnivers.dedeUnivers.services.ProductOptionService;

import java.util.List;

@RestController
@RequestMapping("api/productOption")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ProductOptionController {

    @Autowired
    private ProductOptionService productOptionService;


    @GetMapping("/all/productOption/{idProduct}")
    public ResponseEntity<List<ProductOption>> getAllproductOptionsByProductId(@PathVariable Integer idProduct) {

        try {
            List<ProductOption> productOptions = productOptionService.getAllByProductId(idProduct);
            if (productOptions.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productOptions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ProductOption> addProductOption(@RequestBody ProductOption productOption){
        try {
            if (productOption == null) {

                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            ProductOption _productOption = productOptionService.save(productOption);
            if (_productOption != null) {
                return new ResponseEntity<>(_productOption, HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update")
    public ResponseEntity<String>updateProductOption(@RequestBody ProductOption productOption){
        try {
            if (productOption == null|| productOption.getId() <= 0
            ) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            ProductOption _productOption = productOptionService.save(productOption);
            if (_productOption != null) {
                return new ResponseEntity<String>("Le product Option a pu etre modifier!", HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeColor(@PathVariable("id") Integer id) {

        try {
            if (id <= 0) {
                return new ResponseEntity<String>("Erreur : L'id du  product Option à supprimer doit être > 0 !",
                        HttpStatus.BAD_REQUEST);
            }
            productOptionService.remove(id);
            return new ResponseEntity<String>("Suppression du  product Option avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}



