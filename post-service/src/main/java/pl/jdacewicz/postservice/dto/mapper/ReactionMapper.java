package pl.jdacewicz.postservice.dto.mapper;

import org.springframework.stereotype.Component;
import pl.jdacewicz.postservice.dto.ReactionDto;
import pl.jdacewicz.postservice.dto.ReactionRequest;
import pl.jdacewicz.postservice.model.Reaction;

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
}
