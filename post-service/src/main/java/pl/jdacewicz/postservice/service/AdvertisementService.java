package pl.jdacewicz.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.exception.RecordNotFoundException;
import pl.jdacewicz.postservice.model.Advertisement;
import pl.jdacewicz.postservice.repository.AdvertisementRepository;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    public Advertisement createAdvertisement(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    public Advertisement getAdvertisementById(int id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find advertisement with id: " + id));
    }
}
