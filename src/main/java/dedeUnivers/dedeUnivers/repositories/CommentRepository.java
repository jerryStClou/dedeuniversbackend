package dedeUnivers.dedeUnivers.repositories;

import java.util.List;
import java.util.Set;

import dedeUnivers.dedeUnivers.projections.CommentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository   extends JpaRepository<Comment, Integer> {
    //
    List<CommentProjection> findByUserId(Integer userId);
    //
    List<CommentProjection> findByProductId(Integer productId);

    // Utiliser une méthode avec un type de projection
    @Query("SELECT c.id AS id, c.comment AS comment, c.titleComment AS titleComment, c.note AS note, c.user AS user FROM Comment c WHERE c.id = :id")
    CommentProjection findProjectionById(@Param("id") Integer id);

}


