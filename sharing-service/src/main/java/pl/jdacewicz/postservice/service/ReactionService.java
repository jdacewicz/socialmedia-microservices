package pl.jdacewicz.postservice.service;

import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jdacewicz.postservice.exception.RecordNotFoundException;
import pl.jdacewicz.postservice.model.Reaction;
import pl.jdacewicz.postservice.repository.ReactionRepository;
import pl.jdacewicz.postservice.util.PageableUtils;

@Service
public class ReactionService {

    private final ReactionRepository reactionRepository;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    public Reaction getReactionById(int id) {
        return reactionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find reaction with id: " + id));
    }

    public Page<Reaction> getReactions(String name, int page, int size, String sort, String directory) {
        Pageable paging = PageableUtils.createPageable(page, size, sort, directory);
        if (name == null || name.isEmpty()) {
            return reactionRepository.findAll(paging);
        } else {
            return reactionRepository.findAllByName(name, paging);
        }
    }

    public Reaction createReaction(Reaction reaction) {
        return reactionRepository.save(reaction);
    }

    @Transient
    public void updateReaction(int id, Reaction reaction) {
        reactionRepository.setReactionById(id, reaction);
    }

    public void deleteReaction(int id) {
        reactionRepository.deleteById(id);
    }
}
