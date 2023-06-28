package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.AdvertisementDto;
import pl.jdacewicz.postservice.dto.AdvertisementRequest;
import pl.jdacewicz.postservice.dto.CommentRequest;
import pl.jdacewicz.postservice.dto.mapper.AdvertisementMapper;
import pl.jdacewicz.postservice.dto.mapper.CommentMapper;
import pl.jdacewicz.postservice.model.Advertisement;
import pl.jdacewicz.postservice.model.Comment;
import pl.jdacewicz.postservice.service.AdvertisementService;

@RestController
@RequestMapping("/api/advertisements")
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
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementDto getAdvertisementById(@PathVariable int id) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        return advertisementMapper.convertToDto(advertisement);
    }

    @GetMapping
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
    @ResponseStatus(HttpStatus.OK)
    public void reactToPost(@PathVariable int advertisementId,
                            @PathVariable int reactionId) {
        advertisementService.reactToAdvertisement(advertisementId, reactionId);
    }

    @PutMapping("/{advertisementId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public void commentPost(@PathVariable int advertisementId,
                            @RequestBody CommentRequest commentRequest) {
        Comment comment = commentMapper.convertFromRequest(commentRequest);
        advertisementService.commentAdvertisement(advertisementId, comment);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementDto createAdvertisement(@RequestBody AdvertisementRequest advertisementRequest) {
        Advertisement advertisement = advertisementMapper.convertFromRequest(advertisementRequest);
        Advertisement createdAdvertisement = advertisementService.createAdvertisement(advertisement);
        return advertisementMapper.convertToDto(createdAdvertisement);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateAdvertisement(@PathVariable int id,
                                    @RequestBody AdvertisementRequest advertisementRequest) {
        Advertisement advertisement = advertisementMapper.convertFromRequest(advertisementRequest);
        advertisementService.updateAdvertisement(id, advertisement);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAdvertisement(@PathVariable int id) {
        advertisementService.deleteAdvertisement(id);
    }
}
