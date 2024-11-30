package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Comment;
import dedeUnivers.dedeUnivers.entities.Material;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.User;
import dedeUnivers.dedeUnivers.services.CommentService;
import dedeUnivers.dedeUnivers.services.ProductService;
import dedeUnivers.dedeUnivers.services.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/comment")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    UserService userService;

    @Autowired
    ProductService productService;


    @GetMapping("/all/user/{idUser}")
    public ResponseEntity<List<Comment>> getAllCommentsByUserId(@PathVariable Integer idUser) {

        try {
            List<Comment> comments = commentService.getAllCommentByUserId(idUser);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //
    @GetMapping("/all/product/{idProduct}")
    public ResponseEntity<List<Comment>> getAllCommentsByProductId(@PathVariable Integer idProduct) {

        try {
            List<Comment> comments = commentService.getAllCommentByProductId(idProduct);
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id")  Integer id) {
        try{
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            else {
                Comment comment = commentService.findById(id);

                return new ResponseEntity<>(comment, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/add/{idUser}/{idProduct}")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment,@PathVariable("idUser") Integer idUser,@PathVariable("idProduct") Integer idProduct){
        try {

            User user = userService.findById(idUser);
            Product product = productService.findById(idProduct);

            comment.setUser(user);
            comment.setProduct(product);

            Comment _comment = commentService.save(comment);

            return new ResponseEntity<>(_comment, HttpStatus.CREATED);

        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    @PutMapping("/update/{idComment}")
    public ResponseEntity<String>updateComment(@RequestBody Comment comment,@PathVariable Integer idComment){
        try {
            Comment comment1 = commentService.findById(idComment);
            comment1.setComment(comment.getComment());
            comment1.setImageComment(comment.getImageComment());
            comment1.setTitleComment(comment.getTitleComment());
            comment1.setNote(comment.getNote());
            Comment _comment = commentService.save(comment1);
            if (_comment != null) {
                return new ResponseEntity<String>("Le commentaire à pu être modifier!", HttpStatus.CREATED);
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
            commentService.remove(id);
            return new ResponseEntity<String>("Suppression du comment avec id = '" + id + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }






}
