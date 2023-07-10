package pl.jdacewicz.sharingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.sharingservice.dto.ReactionDto;
import pl.jdacewicz.sharingservice.dto.ReactionRequest;
import pl.jdacewicz.sharingservice.dto.mapper.ReactionMapper;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.service.ReactionService;
import pl.jdacewicz.sharingservice.util.ApiVersion;

@RestController
@Transactional
@RequestMapping(value = "${spring.application.api-url}" + "/reactions",
        produces = {ApiVersion.V1_JSON})
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

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionDto createReaction(@RequestBody ReactionRequest reactionRequest) {
        Reaction reaction = reactionMapper.convertFromRequest(reactionRequest);
        Reaction createdReaction =  reactionService.createReaction(reaction);
        return reactionMapper.convertToDto(createdReaction);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void updateReaction(@PathVariable int id,
                                      @RequestBody ReactionRequest reactionRequest) {
        Reaction reaction = reactionMapper.convertFromRequest(reactionRequest);
        reactionService.updateReaction(id, reaction);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReaction(@PathVariable int id) {
        reactionService.deleteReaction(id);
    }
}
