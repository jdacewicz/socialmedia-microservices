package pl.jdacewicz.postservice.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.postservice.dto.PostDto;
import pl.jdacewicz.postservice.dto.PostRequest;
import pl.jdacewicz.postservice.model.Post;

@Component
public class PostMapper {

    private final ReactionMapper reactionMapper;

    @Autowired
    public PostMapper(ReactionMapper reactionMapper) {
        this.reactionMapper = reactionMapper;
    }

    public PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .creationTime(post.getCreationTime())
                .content(post.getContent())
                .reactions(reactionMapper.convertToCountDto(post.getReactions()))
                .build();
    }

    public Post convertFromRequest(PostRequest postRequest) {
        return Post.builder()
                .content(postRequest.content())
                .build();
    }
}
