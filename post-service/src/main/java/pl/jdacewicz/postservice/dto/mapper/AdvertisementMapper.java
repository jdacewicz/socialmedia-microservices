package pl.jdacewicz.postservice.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.postservice.dto.AdvertisementDto;
import pl.jdacewicz.postservice.dto.AdvertisementRequest;
import pl.jdacewicz.postservice.model.Advertisement;

@Component
public class AdvertisementMapper {

    private final ReactionMapper reactionMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public AdvertisementMapper(ReactionMapper reactionMapper, CommentMapper commentMapper) {
        this.reactionMapper = reactionMapper;
        this.commentMapper = commentMapper;
    }

    public Advertisement convertFromRequest(AdvertisementRequest advertisementRequest) {
        return Advertisement.builder()
                .name(advertisementRequest.name())
                .content(advertisementRequest.content())
                .build();
    }

    public AdvertisementDto convertToDto(Advertisement createdAdvertisement) {
        return AdvertisementDto.builder()
                .name(createdAdvertisement.getName())
                .content(createdAdvertisement.getContent())
                .reactions(reactionMapper.convertToCountDto(createdAdvertisement.getReactions()))
                .comments(commentMapper.convertToDto(createdAdvertisement.getComments()
                        .stream()
                        .limit(2)
                        .toList()))
                .build();
    }
}
