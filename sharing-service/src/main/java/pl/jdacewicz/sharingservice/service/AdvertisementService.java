package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.model.Comment;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.repository.AdvertisementRepository;
import pl.jdacewicz.sharingservice.util.PageableUtils;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final ReactionService reactionService;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository, ReactionService reactionService) {
        this.advertisementRepository = advertisementRepository;
        this.reactionService = reactionService;
    }

    public Advertisement getAdvertisementById(int id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find advertisement with id: " + id));
    }

    public Page<Advertisement> getAdvertisements(String name, int page, int size, String sort, String directory) {
        Pageable paging = PageableUtils.createPageable(page, size, sort, directory);
        if (name == null || name.isEmpty()) {
            return advertisementRepository.findAll(paging);
        } else {
            return advertisementRepository.findAllByName(name, paging);
        }
    }

    public Advertisement createAdvertisement(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    public void reactToAdvertisement(int advertisementId, int reactionId) {
        Reaction reaction = reactionService.getReactionById(reactionId);

        advertisementRepository.findById(advertisementId)
                .map(ad -> {
                    ad.addReaction(reaction);
                    return advertisementRepository.save(ad);
                }).orElseThrow(() -> new RecordNotFoundException("Could not find advertisement with id: " + advertisementId));
    }

    public void commentAdvertisement(int advertisementId, Comment comment) {
        advertisementRepository.findById(advertisementId)
                .map(ad -> {
                    ad.addComment(comment);
                    return advertisementRepository.save(ad);
                }).orElseThrow(() -> new RecordNotFoundException("Could not find advertisement with id: " + advertisementId));
    }

    public void updateAdvertisement(int id, Advertisement advertisement) {
        advertisementRepository.findById(id).map(ad -> {
            ad.setName(advertisement.getName());
            ad.setContent(ad.getContent());
            return advertisementRepository.save(ad);
        }).orElseThrow(() -> new RecordNotFoundException("Could not find advertisement with id: " + id));
    }

    public void deleteAdvertisement(int id) {
        advertisementRepository.deleteById(id);
    }
}
