package pl.jdacewicz.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.postservice.entity.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
}
