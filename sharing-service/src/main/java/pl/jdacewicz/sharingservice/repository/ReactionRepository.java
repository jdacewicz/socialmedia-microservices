package pl.jdacewicz.sharingservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.sharingservice.model.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {

    Page<Reaction> findAllByName(String name, Pageable pageable);
}
