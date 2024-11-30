package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    @Size(max = 255)
    private String comment;

    @Column(name = "title_comment", nullable = true, length = 100)
    @Size(min = 2, max = 100)
    private String titleComment;

    @Column(name = "note")
    @Size(min = 0, max = 5)
    private int note;


    @Column(name = "image_comment")
    private String imageComment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Comment() {
    }

    public Comment(int id, String comment, String titleComment, int note, String imageComment) {
        this.id = id;
        this.comment = comment;
        this.titleComment = titleComment;
        this.note = note;
        this.imageComment = imageComment;
    }
}

