package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.AdvertisementRepository;
import pl.jdacewicz.sharingservice.util.PageableUtils;

@Service
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final ReactionService reactionService;
    private final UserService userService;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository, ReactionService reactionService,
                                UserService userService) {
        this.advertisementRepository = advertisementRepository;
        this.reactionService = reactionService;
        this.userService = userService;
    }

    public Advertisement getActiveAdvertisementById(int id) {
        return advertisementRepository.findByIdAndActive(id, true)
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

    public Advertisement createAdvertisement(String userEmail, String name, String content, String imageFileName) {
        User user = userService.getUserByEmail(userEmail);
        Advertisement advertisement = Advertisement.builder()
                .name(name)
                .content(content)
                .image(imageFileName)
                .creator(user)
                .build();
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

    public Advertisement updateAdvertisement(String userEmail, int id, String name, String content) {
        User user = userService.getUserByEmail(userEmail);
        return advertisementRepository.findById(id).map(ad -> {
            ad.setName(name);
            ad.setContent(content);
            ad.setCreator(user);
            return advertisementRepository.save(ad);
        }).orElseThrow(() -> new RecordNotFoundException("Could not find advertisement with id: " + id));
    }

    public void deleteAdvertisement(int id) {
        advertisementRepository.deleteById(id);
    }
}
