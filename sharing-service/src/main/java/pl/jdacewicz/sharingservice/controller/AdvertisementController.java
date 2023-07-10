package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.sharingservice.dto.AdvertisementDto;
import pl.jdacewicz.sharingservice.dto.AdvertisementRequest;
import pl.jdacewicz.sharingservice.dto.CommentRequest;
import pl.jdacewicz.sharingservice.dto.mapper.AdvertisementMapper;
import pl.jdacewicz.sharingservice.dto.mapper.CommentMapper;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.model.Comment;
import pl.jdacewicz.sharingservice.service.AdvertisementService;
import pl.jdacewicz.sharingservice.util.ApiVersion;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/advertisements",
        produces = {ApiVersion.V1_JSON})
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, AdvertisementMapper advertisementMapper,
                                   CommentMapper commentMapper) {
        this.advertisementService = advertisementService;
        this.advertisementMapper = advertisementMapper;
        this.commentMapper = commentMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementDto getAdvertisementById(@PathVariable int id) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
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

    @PutMapping("/{advertisementId}/comment")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void commentPost(@PathVariable int advertisementId,
                            @RequestBody CommentRequest commentRequest) {
        Comment comment = commentMapper.convertFromRequest(commentRequest);
        advertisementService.commentAdvertisement(advertisementId, comment);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementDto createAdvertisement(@RequestBody AdvertisementRequest advertisementRequest) {
        Advertisement advertisement = advertisementMapper.convertFromRequest(advertisementRequest);
        Advertisement createdAdvertisement = advertisementService.createAdvertisement(advertisement);
        return advertisementMapper.convertToDto(createdAdvertisement);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void updateAdvertisement(@PathVariable int id,
                                    @RequestBody AdvertisementRequest advertisementRequest) {
        Advertisement advertisement = advertisementMapper.convertFromRequest(advertisementRequest);
        advertisementService.updateAdvertisement(id, advertisement);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdvertisement(@PathVariable int id) {
        advertisementService.deleteAdvertisement(id);
    }
}
