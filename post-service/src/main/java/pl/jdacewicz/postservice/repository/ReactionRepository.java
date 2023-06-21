package pl.jdacewicz.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.jdacewicz.postservice.model.Reaction;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {

    Page<Reaction> findAll(Pageable pageable);

    Page<Reaction> findAllByName(String name, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Reaction r " +
            "SET r.name = :#{#reaction.name} " +
            "WHERE r.id = ?1")
    void setReactionById(int id, Reaction reaction);
}
