package dedeUnivers.dedeUnivers.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Comment;

public interface CommentRepository   extends JpaRepository<Comment, Integer> {
    //
    List<Comment> findByUserId(Integer userId);
    //
    List<Comment> findByProductId(Integer productId);

}

