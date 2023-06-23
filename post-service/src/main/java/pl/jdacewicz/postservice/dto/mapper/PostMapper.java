package pl.jdacewicz.postservice.dto.mapper;

import org.springframework.stereotype.Component;
import pl.jdacewicz.postservice.dto.PostDto;
import pl.jdacewicz.postservice.dto.PostReactionDto;
import pl.jdacewicz.postservice.dto.PostRequest;
import pl.jdacewicz.postservice.model.Post;
import pl.jdacewicz.postservice.model.Reaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PostMapper {

    public PostDto convertToDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .creationTime(post.getCreationTime())
                .content(post.getContent())
                .reactions(convertReactionToDto(post.getReactions()))
                .build();
    }

    public Post convertFromRequest(PostRequest postRequest) {
        return Post.builder()
                .content(postRequest.content())
                .build();
    }

    private List<PostReactionDto> convertReactionToDto(List<Reaction> reactionList) {
        Set<Reaction> reactionSet = new HashSet<>(reactionList);

        return reactionSet.stream()
                .map(reaction -> PostReactionDto.builder()
                        .name(reaction.getName())
                        .count(countReactions(reactionList, reaction))
                        .build())
                .toList();
    }

    private long countReactions(List<Reaction> reactionList, Reaction reaction) {
        return reactionList.stream()
                .filter(r -> r.equals(reaction))
                .count();
    }
}
