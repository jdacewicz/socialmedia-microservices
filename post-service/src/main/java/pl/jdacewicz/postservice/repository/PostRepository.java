package pl.jdacewicz.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.postservice.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
