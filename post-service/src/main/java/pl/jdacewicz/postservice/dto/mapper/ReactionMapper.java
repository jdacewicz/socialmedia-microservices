package pl.jdacewicz.postservice.dto.mapper;

import org.springframework.stereotype.Component;
import pl.jdacewicz.postservice.dto.ReactionCountDto;
import pl.jdacewicz.postservice.dto.ReactionDto;
import pl.jdacewicz.postservice.dto.ReactionRequest;
import pl.jdacewicz.postservice.model.Reaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ReactionMapper {
    public Reaction convertFromRequest(ReactionRequest reactionRequest) {
        return Reaction.builder()
                .name(reactionRequest.name())
                .build();
    }

    public ReactionDto convertToDto(Reaction reaction) {
        return ReactionDto.builder()
                .name(reaction.getName())
                .build();
    }

    public List<ReactionCountDto> convertToCountDto(List<Reaction> reactionList) {
        Set<Reaction> reactionSet = new HashSet<>(reactionList);

        return reactionSet.stream()
                .map(reaction -> ReactionCountDto.builder()
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
