package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.AdvertisementRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;
import pl.jdacewicz.sharingservice.util.PageableUtils;

import java.io.IOException;

@Service
public class AdvertisementService {

    @Value("${message.not-found.advertisement}")
    private String notFoundMessage;

    private final AdvertisementRepository advertisementRepository;
    private final UserService userService;

    @Autowired
    public AdvertisementService(AdvertisementRepository advertisementRepository, UserService userService) {
        this.advertisementRepository = advertisementRepository;
        this.userService = userService;
    }

    public Advertisement getAdvertisementById(int id) {
        return advertisementRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

    public Advertisement getActiveAdvertisementById(int id) {
        return advertisementRepository.findByIdAndActive(id, true)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

    public Page<Advertisement> getAdvertisements(String name, int page, int size, String sort, String directory) {
        Pageable paging = PageableUtils.createPageable(page, size, sort, directory);
        return (name == null || name.isEmpty()) ?
                advertisementRepository.findAll(paging) : advertisementRepository.findAllByName(name, paging);
    }

    public Advertisement createAdvertisement(String userEmail, String name, String content, MultipartFile image)
            throws IOException {
        User user = userService.getUserByEmail(userEmail);
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        Advertisement ad = Advertisement.builder()
                .name(name)
                .content(content)
                .image(newFileName)
                .creator(user)
                .build();
        Advertisement createdAd = advertisementRepository.save(ad);

        FileUtils.saveFile(image, newFileName, createdAd.getDirectoryPath());
        return createdAd;
    }

    public Advertisement updateAdvertisement(String userEmail, int id, String name, String content, MultipartFile image)
            throws IOException {
        User user = userService.getUserByEmail(userEmail);

        Advertisement advertisement = advertisementRepository.findById(id)
                .map(ad -> {
                    ad.setName(name);
                    ad.setContent(content);
                    ad.setCreator(user);
                    return ad;
                }).orElseThrow(() -> new RecordNotFoundException(notFoundMessage));

        FileUtils.saveFile(image, advertisement.getImage(), advertisement.getDirectoryPath());
        return advertisementRepository.save(advertisement);
    }

    public void deleteAdvertisement(int id) throws IOException {
        Advertisement advertisement = getAdvertisementById(id);

        FileUtils.deleteDirectory(advertisement.getDirectoryPath());
        advertisementRepository.delete(advertisement);
    }
}
