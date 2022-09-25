package jstagram.server.repository;

import java.util.List;
import jstagram.server.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.user.id = ?1 order by p.createdAt DESC")
    List<Post> findByUserId(Long userId);
}
