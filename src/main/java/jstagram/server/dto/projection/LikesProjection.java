package jstagram.server.dto.projection;

public interface LikesProjection {

    Long getLikesId();

    Long getPostId();

    Long getUserId();

    Long getLikes();

    Long getLiked();
}
