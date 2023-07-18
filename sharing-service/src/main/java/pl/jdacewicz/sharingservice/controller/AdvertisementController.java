package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.AdvertisementDto;
import pl.jdacewicz.sharingservice.dto.mapper.AdvertisementMapper;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.service.AdvertisementService;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementDto createAdvertisement(@RequestPart String userEmail,
                                                @RequestPart String name,
                                                @RequestPart String content,
                                                @RequestPart MultipartFile image) throws IOException {
        Advertisement createdAd = advertisementService.createAdvertisement(userEmail, name, content, image);
        return advertisementMapper.convertToDto(createdAd);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementDto updateAdvertisement(@PathVariable int id,
                                                @RequestPart String userEmail,
                                                @RequestPart String name,
                                                @RequestPart String content,
                                                @RequestPart MultipartFile image) throws IOException {
        Advertisement updatedAd = advertisementService.updateAdvertisement(userEmail, id, name, content, image);
        return advertisementMapper.convertToDto(updatedAd);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdvertisement(@PathVariable int id) throws IOException {
        advertisementService.deleteAdvertisement(id);
    }
}
