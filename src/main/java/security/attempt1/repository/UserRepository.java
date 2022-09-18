package security.attempt1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import security.attempt1.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
