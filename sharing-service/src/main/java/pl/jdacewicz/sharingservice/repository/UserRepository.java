package pl.jdacewicz.sharingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.sharingservice.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
