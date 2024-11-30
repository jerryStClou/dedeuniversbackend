package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Material;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductSize;
import dedeUnivers.dedeUnivers.services.ProductService;
import dedeUnivers.dedeUnivers.services.ProductSizeService;

import java.util.List;

@RestController
@RequestMapping("api/productSize")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ProductSizeController {

    @Autowired
    private ProductSizeService productSizeService;

    @Autowired
    ProductService productService;

    @GetMapping("/all/{idProduct}")
    public ResponseEntity<List<ProductSize>> getAllProductSizesByProductId(@PathVariable Integer idProduct) {

        try {
            List<ProductSize> productSizes = productSizeService.getAllByProductId(idProduct);
            if (productSizes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productSizes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductSize> getProductSizeById(@PathVariable("id")  Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                ProductSize productSize = productSizeService.findById(id);
                return new ResponseEntity<>(productSize, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/add/{idProductOption}")
    public ResponseEntity<ProductSize> addProductSize(@RequestBody ProductSize productSize,@PathVariable Integer idProductOption){
        try {
            if (productSize == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            ProductSize _productSize = productSizeService.save(productSize);
            if (_productSize != null) {
                return new ResponseEntity<>(_productSize, HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idProductSize}")
    public ResponseEntity<String>updateProductSize(@RequestBody ProductSize productSize, @PathVariable Integer idProductSize){
        try {

            ProductSize productSize1 = productSizeService.findById(idProductSize);
            productSize1.setProductSize(productSize.getProductSize());
            productSize1.setInfluenceProductSizePrice(productSize.getInfluenceProductSizePrice());
            productSize1.setInfluenceProductSizeWeight(productSize.getInfluenceProductSizeWeight());
            ProductSize _productSize = productSizeService.save(productSize1);
            if (_productSize != null) {
                return new ResponseEntity<String>("Le productSize a pu etre modifier!", HttpStatus.CREATED);
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
                return new ResponseEntity<String>("Erreur : L'id du productSize à supprimer doit être > 0 !",
                        HttpStatus.BAD_REQUEST);
            }
            productSizeService.remove(id);
            return new ResponseEntity<String>("Suppression du productSize avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
