package pl.jdacewicz.postservice.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.postservice.dto.PostDto;
import pl.jdacewicz.postservice.dto.PostRequest;
import pl.jdacewicz.postservice.model.Post;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostMapper {

    private final ReactionMapper reactionMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public PostMapper(ReactionMapper reactionMapper, CommentMapper commentMapper) {
        this.reactionMapper = reactionMapper;
        this.commentMapper = commentMapper;
    }

    public PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .creationTime(post.getCreationTime())
                .content(post.getContent())
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

    public Post convertFromRequest(PostRequest postRequest) {
        return Post.builder()
                .content(postRequest.content())
                .build();
    }
}
