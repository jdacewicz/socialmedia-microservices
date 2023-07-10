package pl.jdacewicz.sharingservice.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.CommentDto;
import pl.jdacewicz.sharingservice.dto.CommentRequest;
import pl.jdacewicz.sharingservice.model.Comment;

import java.util.List;

@Component
public class CommentMapper {

    private final ReactionMapper reactionMapper;

    @Autowired
    public CommentMapper(ReactionMapper reactionMapper) {
        this.reactionMapper = reactionMapper;
    }

    public CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .creationTime(comment.getCreationTime())
                .reactions(reactionMapper.convertToCountDto(comment.getReactions()))
                .content(comment.getContent())
                .build();
    }

    public List<CommentDto> convertToDto(List<Comment> commentList) {
        return commentList.stream()
                .map(this::convertToDto)
                .toList();
    }

    public Comment convertFromRequest(CommentRequest commentRequest) {
        return Comment.builder()
                .content(commentRequest.content())
                .build();
    }
}
