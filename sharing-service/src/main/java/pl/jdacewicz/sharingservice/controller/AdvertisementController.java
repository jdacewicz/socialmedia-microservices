package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.AdvertisementDto;
import pl.jdacewicz.sharingservice.dto.mapper.AdvertisementMapper;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.service.AdvertisementService;
import pl.jdacewicz.sharingservice.util.FileUtils;

import java.beans.Transient;
import java.io.IOException;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/advertisements",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService,
                                   AdvertisementMapper advertisementMapper) {
        this.advertisementService = advertisementService;
        this.advertisementMapper = advertisementMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementDto getActiveAdvertisementById(@PathVariable int id) {
        Advertisement advertisement = advertisementService.getActiveAdvertisementById(id);
        return advertisementMapper.convertToDto(advertisement);
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public Page<AdvertisementDto> getAdvertisements(@RequestParam(required = false) String name,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "8") int size,
                                                    @RequestParam(defaultValue = "id") String sort,
                                                    @RequestParam(defaultValue = "ASC") String directory) {
        return advertisementService.getAdvertisements(name, page, size, sort, directory)
                .map(advertisementMapper::convertToDto);
    }

    @PutMapping("/{advertisementId}/react/{reactionId}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void reactToPost(@PathVariable int advertisementId,
                            @PathVariable int reactionId) {
        advertisementService.reactToAdvertisement(advertisementId, reactionId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    @Transient
    public AdvertisementDto createAdvertisement(@AuthenticationPrincipal Jwt jwt,
                                                @RequestPart String name,
                                                @RequestPart String content,
                                                @RequestPart MultipartFile image) throws IOException {
        String userEmail = jwt.getClaim("email");
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());
        Advertisement createdAd = advertisementService.createAdvertisement(userEmail, name, content,
                newFileName);

        FileUtils.saveFile(image, newFileName, createdAd.getDirectoryPath());
        return advertisementMapper.convertToDto(createdAd);
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void updateAdvertisement(@PathVariable int id,
                                    @RequestPart String name,
                                    @RequestPart String content,
                                    @RequestPart MultipartFile image) {
        advertisementService.updateAdvertisement(id, name, content, image);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdvertisement(@PathVariable int id) {
        advertisementService.deleteAdvertisement(id);
    }
}
