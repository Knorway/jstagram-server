package jstagram.server.service;

import java.util.Optional;
import jstagram.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserService extends JpaRepository<User, Long> {

    @Override
    Optional<User> findById(Long id);
}
