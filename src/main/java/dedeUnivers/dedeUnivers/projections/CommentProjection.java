package dedeUnivers.dedeUnivers.projections;

public interface CommentProjection {
    Integer getId();
    String getComment();
    String getTitleComment();
    Integer getNote();
    UserProjection getUser();
}


