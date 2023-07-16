package pl.jdacewicz.sharingservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.sharingservice.model.Advertisement;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    Optional<Advertisement> findByIdAndActive(int id, boolean active);

    Page<Advertisement> findAllByName(String name, Pageable pageable);
}
