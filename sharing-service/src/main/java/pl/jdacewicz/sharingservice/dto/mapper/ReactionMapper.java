package pl.jdacewicz.sharingservice.dto.mapper;

import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.ReactionCountDto;
import pl.jdacewicz.sharingservice.dto.ReactionDto;
import pl.jdacewicz.sharingservice.model.Reaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ReactionMapper {

    public ReactionDto convertToDto(Reaction reaction) {
        return ReactionDto.builder()
                .name(reaction.getName())
                .imagePath(reaction.getImagePath())
                .build();
    }

    public List<ReactionCountDto> convertToCountDto(List<Reaction> reactionList) {
        Set<Reaction> reactionSet = new HashSet<>(reactionList);

        return reactionSet.stream()
                .map(reaction -> ReactionCountDto.builder()
                        .name(reaction.getName())
                        .imagePath(reaction.getImagePath())
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
