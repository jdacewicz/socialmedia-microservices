package pl.jdacewicz.sharingservice.dto.mapper;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.CommentDto;
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
                .imagePath(comment.getImagePath())
                .creator(userMapper.convertToDto(comment.getCreator()))
                .creationTime(comment.getCreationTime())
                .reactions(reactionMapper.convertToCountDto(comment.getReactions()))
                .content(EmojiParser.parseToUnicode(comment.getContent()))
                .build();
    }

    public List<CommentDto> convertToDto(List<Comment> commentList) {
        return commentList.stream()
                .map(this::convertToDto)
                .toList();
    }
}
