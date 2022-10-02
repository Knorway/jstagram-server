package jstagram.server.repository;

import java.util.List;
import jstagram.server.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //    @Query("select c from Comment c where c.post.id = id")
    //    List<Comment> findRelatedCommentFirst3(@Param("id") Long id, Pageable pageable);
    //
    List<Comment> findByPostId(Long id, Pageable pageable);

}
