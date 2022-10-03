package jstagram.server.repository;

import jstagram.server.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query("select l from Likes l where l.post.id = :postId and l.user.id = :userId")
    Likes findByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);
}
