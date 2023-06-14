package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.ReactionRequest;
import pl.jdacewicz.postservice.entity.Reaction;
import pl.jdacewicz.postservice.service.ReactionService;

import java.util.List;

@RestController
@RequestMapping(value = "/api/reactions", headers = "Accept=application/json")
public class ReactionController {

    private final ReactionService reactionService;

    @Autowired
    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Reaction> getAllReactions() {
        return reactionService.getAllReactions();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reaction createReaction(@RequestBody ReactionRequest reactionRequest) {
        return reactionService.createReaction(reactionRequest);
    }
}
