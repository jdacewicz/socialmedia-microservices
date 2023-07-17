package pl.jdacewicz.sharingservice.service;

import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.repository.ReactionRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;
import pl.jdacewicz.sharingservice.util.PageableUtils;

import java.io.IOException;

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

    @Transient
    public Reaction createReaction(String name, MultipartFile image) throws IOException {
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        Reaction reaction = Reaction.builder()
                .name(name)
                .image(newFileName)
                .build();
        Reaction createdReaction = reactionRepository.save(reaction);

        FileUtils.saveFile(image, newFileName, createdReaction.getDirectoryPath());
        return createdReaction;
    }

    @Transient
    public Reaction updateReaction(int id, String name, MultipartFile image) throws IOException {
        Reaction reaction = reactionRepository.findById(id)
                .map(r -> {
                    r.setName(name);
                    return r;
                }).orElseThrow(() -> new RecordNotFoundException("Could not find reaction with id: " + id));

        FileUtils.saveFile(image, reaction.getImage(), reaction.getDirectoryPath());
        return reactionRepository.save(reaction);
    }

    public void deleteReaction(int id) {
        reactionRepository.deleteById(id);
    }
}
