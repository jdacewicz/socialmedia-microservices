package pl.jdacewicz.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.jdacewicz.postservice.model.Post;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndVisibleIs(long id, boolean visible);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Post p SET p.visible = ?2 WHERE p.id = ?1")
    void findByIdAndSetVisible(long id, boolean visible);
}
