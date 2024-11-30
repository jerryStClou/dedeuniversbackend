package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductImages;
import dedeUnivers.dedeUnivers.services.ColorService;
import dedeUnivers.dedeUnivers.services.ProductService;

import java.util.List;

@RestController
@RequestMapping("api/color")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ColorController {

    @Autowired
    private ColorService colorService;

    @Autowired
    ProductService productService;


    @GetMapping("/{id}")
    public ResponseEntity<Color> getColorById(@PathVariable("id")  Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                Color color = colorService.findById(id);
                return new ResponseEntity<>(color, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/add/{idProductOption}")
    public ResponseEntity<Color> addColor(@RequestBody Color color,@PathVariable Integer idProductOption){
        try {
            Color _color = colorService.addColor(color,idProductOption);
            if (_color != null) {
                return new ResponseEntity<>(_color, HttpStatus.CREATED);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @PutMapping("/update/{idColor}")
    public ResponseEntity<String>updateColor(@RequestBody Color color,@PathVariable Integer idColor){
        try {
            Color color1 = colorService.findById(idColor);
            color1.setColor(color.getColor());
            Color _color = colorService.save(color1);
            if (_color != null) {
                return new ResponseEntity<String>("Le commentaire a pu etre modifier!", HttpStatus.CREATED);
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
                return new ResponseEntity<String>("Erreur : L'id du color à supprimer doit être > 0 !",
                        HttpStatus.BAD_REQUEST);
            }
            colorService.remove(id);
            return new ResponseEntity<String>("Suppression du color avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
