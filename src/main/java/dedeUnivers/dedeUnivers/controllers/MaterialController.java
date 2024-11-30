package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.Material;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductImages;
import dedeUnivers.dedeUnivers.services.ColorService;
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

    @GetMapping("/all/{idProduct}")
    public ResponseEntity<List<Material>> getAllMaterialsByProductId(@PathVariable Integer idProduct) {

        try {
            List<Material> materials = materialService.getAllByProductId(idProduct);
            if (materials.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(materials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable("id")  Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                Material material = materialService.findById(id);
                return new ResponseEntity<>(material, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/add/{idProductOption}")
    public ResponseEntity<Material> addMaterial(@RequestBody Material material,@PathVariable Integer idProductOption){
        try {
            if (material == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            // Product product = productService.findById(idProduct);
            // material.setProduct(product);
            Material _material = materialService.addMaterial(material,idProductOption);
            if (_material != null) {
                return new ResponseEntity<>(_material, HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @PutMapping("/update/{idMaterial}")
    public ResponseEntity<String>updateMaterial(@RequestBody Material material,@PathVariable Integer idMaterial){
        try {

            Material material1 = materialService.findById(idMaterial);
            material1.setMaterial(material.getMaterial());
            material1.setInfluenceMaterialWeight(material.getInfluenceMaterialWeight());
            material1.setInfluenceMaterialPrice(material.getInfluenceMaterialPrice());

            Material _material = materialService.save(material1);
            if (_material != null) {
                return new ResponseEntity<String>("Le material a pu etre modifier!", HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeMaterial(@PathVariable("id") Integer id) {

        try {
            if (id <= 0) {
                return new ResponseEntity<String>("Erreur : L'id du Material à supprimer doit être > 0 !",
                        HttpStatus.BAD_REQUEST);
            }
            materialService.remove(id);
            return new ResponseEntity<String>("Suppression du material avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

