package pl.jdacewicz.sharingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.sharingservice.model.PostGroup;

public interface PostGroupRepository extends JpaRepository<PostGroup, Long> {
}
