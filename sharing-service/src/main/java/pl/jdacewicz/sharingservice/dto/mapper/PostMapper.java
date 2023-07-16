package pl.jdacewicz.sharingservice.dto.mapper;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.PostDto;
import pl.jdacewicz.sharingservice.model.Post;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final ReactionMapper reactionMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Autowired
    public PostMapper(ReactionMapper reactionMapper, CommentMapper commentMapper, UserMapper userMapper) {
        this.reactionMapper = reactionMapper;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
    }

    public PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .creator(userMapper.convertToDto(post.getCreator()))
                .creationTime(post.getCreationTime())
                .content(EmojiParser.parseToUnicode(post.getContent()))
                .imagePath(post.getImagePath())
                .reactions(reactionMapper.convertToCountDto(post.getReactions()))
                .comments(commentMapper.convertToDto(post.getComments()
                        .stream()
                        .limit(2)
                        .toList()))
                .build();
    }

    public Set<PostDto> convertToDto(List<Post> postList) {
        return postList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toSet());
    }
}
