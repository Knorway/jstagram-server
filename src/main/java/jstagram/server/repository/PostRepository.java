package jstagram.server.repository;

import java.util.Collection;
import java.util.List;
import jstagram.server.domain.Post;
import jstagram.server.dto.projection.CommentProjection;
import jstagram.server.dto.projection.LikesProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(
        "select p from Post p join fetch p.user where p.user.id = :userId order by p.createdAt DESC"
    )
    List<Post> findByUserId(@Param("userId") Long userId);

    @Query("select p from Post p left join fetch p.user u where u.id = :id or u.id in :ids")
    List<Post> findRelatedPosts(
        @Param("id") Long id,
        @Param("ids") Collection<Long> ids,
        Pageable pageable
    );

    @Query(
        nativeQuery = true, value = """
        SELECT c.post_id as postId, c.content, c.user_id as userId, c.username
        FROM post AS p INNER JOIN LATERAL (
        		SELECT c.content, c.post_id, user.id AS user_id, user.username
        		FROM comment AS c INNER JOIN user ON c.user_id = user.id
        		WHERE c.post_id = p.id ORDER BY c.id ASC LIMIT :limit) AS c ON c.post_id = p.id
        WHERE p.id in :postIds
        """
    )
    List<CommentProjection> findInitialComments(
        @Param("limit") int limit,
        @Param("postIds") List<Long> postIds
    );

    @Query(
        nativeQuery = true, value = """
        SELECT post.id AS postId, l.id AS likesId, l.user_id AS userId, count(post.id) AS likes,
        	(SELECT count(likes.id) FROM likes WHERE likes.user_id = :userId AND likes.post_id = post.id) AS liked
        	FROM post INNER JOIN LATERAL (
        			SELECT * FROM likes WHERE likes.post_id = post.id) AS l ON l.post_id = post.id
        	WHERE post.id in :postIds GROUP BY post.id
        """
    )
    List<LikesProjection> findLikesCountAndLiked(
        @Param("userId") Long userId,
        @Param("postIds") List<Long> postIds
    );
}
