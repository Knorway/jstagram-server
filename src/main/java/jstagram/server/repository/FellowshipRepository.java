package jstagram.server.repository;

import java.util.List;
import jstagram.server.domain.Fellowship;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FellowshipRepository extends JpaRepository<Fellowship, Long> {

    @Query("select f from Fellowship f where f.follower.id = :id")
    @EntityGraph(attributePaths = {"follower"})
    List<Fellowship> findFollowingsByUserId(@Param("id") Long id);
}
