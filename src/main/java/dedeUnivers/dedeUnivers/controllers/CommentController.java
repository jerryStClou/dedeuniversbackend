package dedeUnivers.dedeUnivers.controllers;

import dedeUnivers.dedeUnivers.projections.CommentProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Comment;
import dedeUnivers.dedeUnivers.services.CommentService;
import dedeUnivers.dedeUnivers.services.ProductService;
import dedeUnivers.dedeUnivers.services.UserService;

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

    @GetMapping("/all/user/{idUser}")
    public ResponseEntity<List<CommentProjection>> getAllCommentsByUserId(@PathVariable Integer idUser) {
        try {
            // Assainir l'ID de l'utilisateur
            String sanitizedIdUser = sanitizeInput(idUser.toString());
            List<CommentProjection> comments = commentService.getAllCommentByUserId(Integer.parseInt(sanitizedIdUser));
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all/product/{idProduct}")
    public ResponseEntity<List<CommentProjection>> getAllCommentsByProductId(@PathVariable Integer idProduct) {
        try {
            // Assainir l'ID du produit
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            List<CommentProjection> comments = commentService.getAllCommentByProductId(Integer.parseInt(sanitizedIdProduct));
            if (comments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(comments, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentProjection> getCommentById(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID du commentaire
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                CommentProjection comment = commentService.getComment(Integer.parseInt(sanitizedId));
                return new ResponseEntity<>(comment, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{idUser}/{idProduct}")
    public ResponseEntity<Comment> addComment(@RequestBody Comment comment, @PathVariable("idUser") Integer idUser, @PathVariable("idProduct") Integer idProduct) {
        try {
            // Assainir les entrées avant de les utiliser
            String sanitizedIdUser = sanitizeInput(idUser.toString());
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            String sanitizedComment = sanitizeInput(comment.getComment());
            String sanitizedImageComment = sanitizeInput(comment.getImageComment());
            String sanitizedTitleComment = sanitizeInput(comment.getTitleComment());

            // Créer le commentaire avec les données assainies
            comment.setComment(sanitizedComment);
            comment.setImageComment(sanitizedImageComment);
            comment.setTitleComment(sanitizedTitleComment);

            Comment _comment = commentService.addComment(comment, Integer.parseInt(sanitizedIdUser), Integer.parseInt(sanitizedIdProduct));

            return new ResponseEntity<>(_comment, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idComment}")
    public ResponseEntity<String> updateComment(@RequestBody Comment comment, @PathVariable Integer idComment) {
        try {
            // Assainir l'ID du commentaire et les données du corps de la requête
            String sanitizedIdComment = sanitizeInput(idComment.toString());
            String sanitizedComment = sanitizeInput(comment.getComment());
            String sanitizedImageComment = sanitizeInput(comment.getImageComment());
            String sanitizedTitleComment = sanitizeInput(comment.getTitleComment());

            Comment comment1 = commentService.findById(Integer.parseInt(sanitizedIdComment));
            comment1.setComment(sanitizedComment);
            comment1.setImageComment(sanitizedImageComment);
            comment1.setTitleComment(sanitizedTitleComment);
            comment1.setNote(comment.getNote());
            Comment _comment = commentService.save(comment1);

            if (_comment != null) {
                return new ResponseEntity<>("Le commentaire à pu être modifié!", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeComment(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID avant de l'utiliser
            String sanitizedId = sanitizeInput(id.toString());
            if (id <= 0) {
                return new ResponseEntity<>("Erreur : L'id du Comment à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }
            commentService.remove(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>("Suppression du comment avec id = '" + sanitizedId + "' effectuée avec succès.", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
