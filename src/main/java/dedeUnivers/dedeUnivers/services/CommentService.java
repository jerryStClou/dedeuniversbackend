package dedeUnivers.dedeUnivers.services;

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

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findById(Integer id) {
        return commentRepository.findById(id).get();
    }
    //
    public List<Comment> getAllCommentByUserId(Integer userId) {
        return commentRepository.findByUserId(userId);
    }
    //
    public List<Comment> getAllCommentByProductId(Integer productId) {
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
