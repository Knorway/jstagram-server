package jstagram.server.dto.projection;

public interface CommentProjection {

    Long getPostId();

    Long getUserId();

    String getContent();

    String getUsername();
}
