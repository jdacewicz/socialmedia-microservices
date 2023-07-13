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
    private final UserMapper userMapper;

    @Autowired
    public CommentMapper(ReactionMapper reactionMapper, UserMapper userMapper) {
        this.reactionMapper = reactionMapper;
        this.userMapper = userMapper;
    }

    public CommentDto convertToDto(Comment comment) {
        return CommentDto.builder()
                .creator(userMapper.convertToDto(comment.getCreator()))
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
