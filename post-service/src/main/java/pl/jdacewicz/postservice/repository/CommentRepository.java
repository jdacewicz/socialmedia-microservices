package pl.jdacewicz.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pl.jdacewicz.postservice.model.Comment;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndVisible(long id, boolean visible);

    Page<Comment> findAllByPostIdAndVisible(long postId, boolean visible, Pageable pageable);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Comment c SET c.visible = ?2 WHERE c.id = ?1")
    void setVisibilityBy(long id, boolean visible);
}
