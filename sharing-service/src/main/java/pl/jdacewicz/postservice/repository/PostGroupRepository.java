package pl.jdacewicz.postservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.postservice.model.PostGroup;

public interface PostGroupRepository extends JpaRepository<PostGroup, Long> {
}
