package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.ReactionDto;
import pl.jdacewicz.sharingservice.dto.mapper.ReactionMapper;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.service.ReactionService;

import java.io.IOException;

@RestController
@RequestMapping(value = "${spring.application.api-url}" + "/reactions",
        headers = "X-API-VERSION=1",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ReactionController {

    private final ReactionService reactionService;
    private final ReactionMapper reactionMapper;

    @Autowired
    public ReactionController(ReactionService reactionService, ReactionMapper reactionMapper) {
        this.reactionService = reactionService;
        this.reactionMapper = reactionMapper;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public ReactionDto getReaction(@PathVariable int id) {
        Reaction reaction = reactionService.getReactionById(id);
        return reactionMapper.convertToDto(reaction);
    }

    @GetMapping
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public Page<ReactionDto> getAllReactions(@RequestParam(required = false) String name,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "8") int size,
                                             @RequestParam(defaultValue = "id") String sort,
                                             @RequestParam(defaultValue = "ASC") String directory) {
        return reactionService.getReactions(name, page, size, sort, directory)
                .map(reactionMapper::convertToDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionDto createReaction(@RequestPart String name,
                                      @RequestPart MultipartFile image) throws IOException {
        Reaction createdReaction = reactionService.createReaction(name, image);
        return reactionMapper.convertToDto(createdReaction);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public ReactionDto updateReaction(@PathVariable int id,
                                      @RequestPart String name,
                                      @RequestPart MultipartFile image) throws IOException {
        Reaction updatedReaction = reactionService.updateReaction(id, name, image);
        return reactionMapper.convertToDto(updatedReaction);
    }

    @PutMapping("/{reactionId}/posts/{postId}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void reactToPost(@PathVariable long postId,
                            @PathVariable int reactionId) {
        reactionService.reactToVisiblePost(reactionId, postId);
    }

    @PutMapping("/{reactionId}/advertisements/{advertisementId}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void reactToAdvertisement(@PathVariable int advertisementId,
                                     @PathVariable int reactionId) {
        reactionService.reactToActiveAdvertisement(reactionId, advertisementId);
    }

    @PutMapping("/{reactionId}/comments/{commentId}")
    @PreAuthorize("hasRole('user')")
    @ResponseStatus(HttpStatus.OK)
    public void reactToComment(@PathVariable long commentId,
                               @PathVariable int reactionId) {
        reactionService.reactToComment(reactionId, commentId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReaction(@PathVariable int id) throws IOException {
        reactionService.deleteReaction(id);
    }
}
