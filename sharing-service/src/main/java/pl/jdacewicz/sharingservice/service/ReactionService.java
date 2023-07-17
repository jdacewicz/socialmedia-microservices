package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.repository.ReactionRepository;
import pl.jdacewicz.sharingservice.util.PageableUtils;

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

    public Reaction createReaction(String name, String newFileName) {
        Reaction reaction = Reaction.builder()
                .name(name)
                .image(newFileName)
                .build();
        return reactionRepository.save(reaction);
    }

    public Reaction updateReaction(int id, String name) {
        return reactionRepository.findById(id).map(reaction -> {
            reaction.setName(name);
            return reactionRepository.save(reaction);
        }).orElseThrow(() -> new RecordNotFoundException("Could not find reaction with id: " + id));
    }

    public void deleteReaction(int id) {
        reactionRepository.deleteById(id);
    }
}
