package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.ReactionDto;
import pl.jdacewicz.postservice.dto.ReactionRequest;
import pl.jdacewicz.postservice.dto.mapper.ReactionMapper;
import pl.jdacewicz.postservice.model.Reaction;
import pl.jdacewicz.postservice.service.ReactionService;

@RestController
@Transactional
@RequestMapping(value = "/api/reactions", headers = "Accept=application/json")
public class ReactionController {

    private final ReactionService reactionService;
    private final ReactionMapper reactionMapper;

    @Autowired
    public ReactionController(ReactionService reactionService, ReactionMapper reactionMapper) {
        this.reactionService = reactionService;
        this.reactionMapper = reactionMapper;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReactionDto getReaction(@PathVariable int id) {
        Reaction reaction = reactionService.getReactionById(id);
        return reactionMapper.convertToDto(reaction);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<ReactionDto> getAllReactions(@RequestParam(required = false) String name,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "8") int size,
                                             @RequestParam(defaultValue = "id") String sort,
                                             @RequestParam(defaultValue = "false") boolean desc) {
        return reactionService.getReactions(name, page, size, sort, desc)
                .map(reactionMapper::convertToDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReactionDto createReaction(@RequestBody ReactionRequest reactionRequest) {
        Reaction reaction = reactionMapper.convertFromRequest(reactionRequest);
        Reaction createdReaction =  reactionService.createReaction(reaction);
        return reactionMapper.convertToDto(createdReaction);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateReaction(@PathVariable int id,
                                      @RequestBody ReactionRequest reactionRequest) {
        Reaction reaction = reactionMapper.convertFromRequest(reactionRequest);
        reactionService.updateReaction(id, reaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteReaction(@PathVariable int id) {
        reactionService.deleteReaction(id);
    }
}
