package dedeUnivers.dedeUnivers.services;

import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.User;
import dedeUnivers.dedeUnivers.projections.CommentProjection;
import dedeUnivers.dedeUnivers.repositories.ProductRepository;
import dedeUnivers.dedeUnivers.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Comment;
import dedeUnivers.dedeUnivers.repositories.CommentRepository;

import java.util.List;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }


    public Comment addComment(Comment comment,Integer idUser,Integer idProduct) {
        User user = userRepository.findById(idUser).get();
        Product product = productRepository.findById(idProduct).get();

        // Assigner l'utilisateur et le produit au commentaire
        comment.setUser(user);
        comment.setProduct(product);

        return commentRepository.save(comment);
    }

    public CommentProjection getComment(Integer id) {
        return commentRepository.findProjectionById(id);
    }


    public Comment findById(Integer id) {
        return commentRepository.findById(id).get();
    }


    //
    public List<CommentProjection> getAllCommentByUserId(Integer userId) {
        return commentRepository.findByUserId(userId);
    }
    //
    public List<CommentProjection> getAllCommentByProductId(Integer productId) {
        return commentRepository.findByProductId(productId);
    }

//
//    public List<Comment> getAllCommentByProductId(int productId) {
//        return commentRepository.findByProductId(productId);
//    }

    public void remove(Integer id) {
        commentRepository.deleteById(id);
    }
}
