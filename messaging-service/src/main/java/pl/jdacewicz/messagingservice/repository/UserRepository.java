package pl.jdacewicz.messagingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.messagingservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
