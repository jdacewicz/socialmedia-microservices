package pl.jdacewicz.postservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.dto.ReactionRequest;
import pl.jdacewicz.postservice.entity.Reaction;
import pl.jdacewicz.postservice.repository.ReactionRepository;

import java.util.List;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public List<Reaction> getAllReactions() {
        return reactionRepository.findAll();
    }

    public Reaction createReaction(ReactionRequest reactionRequest) {
        Reaction reaction = Reaction.builder()
                .name(reactionRequest.name())
                .build();
        return reactionRepository.save(reaction);
    }
}
