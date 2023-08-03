package pl.jdacewicz.sharingservice.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.dto.ReactionRequest;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.model.Comment;
import pl.jdacewicz.sharingservice.model.Post;
import pl.jdacewicz.sharingservice.model.Reaction;
import pl.jdacewicz.sharingservice.repository.ReactionRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;
import pl.jdacewicz.sharingservice.util.PageableUtils;

import java.io.IOException;

@Service
public class ReactionService {

    @Value("${message.reaction.not-found}")
    private String notFoundMessage;

    private final ReactionRepository reactionRepository;
    private final AdvertisementService advertisementService;
    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public ReactionService(ReactionRepository reactionRepository, AdvertisementService advertisementService,
                           CommentService commentService, PostService postService) {
        this.reactionRepository = reactionRepository;
        this.advertisementService = advertisementService;
        this.commentService = commentService;
        this.postService = postService;
    }

    public Reaction getReactionById(int id) {
        return reactionRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

    public Page<Reaction> getReactions(String name, int page, int size, String sort, String directory) {
        Pageable paging = PageableUtils.createPageable(page, size, sort, directory);
        return (StringUtils.isBlank(name)) ?
                reactionRepository.findAll(paging) : reactionRepository.findAllByName(name, paging);
    }

    public Reaction createReaction(ReactionRequest request, MultipartFile image) throws IOException {
        String newFileName = FileUtils.generateFileName(image.getOriginalFilename());

        Reaction reaction = Reaction.builder()
                .name(request.name())
                .image(newFileName)
                .build();
        Reaction createdReaction = reactionRepository.save(reaction);

        FileUtils.saveFile(image, newFileName, createdReaction.getDirectoryPath());
        return createdReaction;
    }

    public void reactToVisiblePost(int reactionId, long postId) {
        Reaction reaction = getReactionById(reactionId);
        Post post = postService.getVisiblePostById(postId);

        post.addReaction(reaction);
        reactionRepository.save(reaction);
    }

    public void reactToActiveAdvertisement(int reactionId, int advertisementId) {
        Reaction reaction = getReactionById(reactionId);
        Advertisement advertisement = advertisementService.getActiveAdvertisementById(advertisementId);

        advertisement.addReaction(reaction);
        reactionRepository.save(reaction);
    }

    public void reactToComment(int reactionId, long commentId) {
        Reaction reaction = getReactionById(reactionId);
        Comment comment = commentService.getCommentById(commentId);

        comment.addReaction(reaction);
        reactionRepository.save(reaction);
    }

    public Reaction updateReaction(int id, ReactionRequest request, MultipartFile image) throws IOException {
        Reaction reaction = getReactionById(id);

        reaction.setName(request.name());
        FileUtils.saveFile(image, reaction.getImage(), reaction.getDirectoryPath());
        return reactionRepository.save(reaction);
    }

    public void deleteReaction(int id) throws IOException {
        Reaction reaction = getReactionById(id);

        FileUtils.deleteDirectory(reaction.getDirectoryPath());
        reactionRepository.deleteById(id);
    }
}
