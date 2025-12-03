package vn.hcmute.eatandorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hcmute.eatandorder.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username, String password);
}


