package pl.jdacewicz.postservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.jdacewicz.postservice.model.Advertisement;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Integer> {

    Page<Advertisement> findAllByName(String name, Pageable pageable);
}
