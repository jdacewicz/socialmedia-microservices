package pl.jdacewicz.postservice.dto.mapper;

import org.springframework.stereotype.Component;
import pl.jdacewicz.postservice.dto.PostDto;
import pl.jdacewicz.postservice.dto.PostRequest;
import pl.jdacewicz.postservice.model.Post;

@Component
public class PostMapper {

    public PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .creationTime(post.getCreationTime())
                .content(post.getContent())
                .build();
    }

    public Post convertFromRequest(PostRequest postRequest) {
        return Post.builder()
                .content(postRequest.content())
                .build();
    }
}
