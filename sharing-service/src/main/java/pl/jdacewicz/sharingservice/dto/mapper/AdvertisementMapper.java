package pl.jdacewicz.sharingservice.dto.mapper;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.AdvertisementDto;
import pl.jdacewicz.sharingservice.model.Advertisement;

@Component
public class AdvertisementMapper {

    private final ReactionMapper reactionMapper;
    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    @Autowired
    public AdvertisementMapper(ReactionMapper reactionMapper, CommentMapper commentMapper,
                               UserMapper userMapper) {
        this.reactionMapper = reactionMapper;
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
    }

    public AdvertisementDto convertToDto(Advertisement advertisement) {
        return AdvertisementDto.builder()
                .name(advertisement.getName())
                .content(EmojiParser.parseToUnicode(advertisement.getContent()))
                .imagePath(advertisement.getImagePath())
                .creator(userMapper.convertToDto(advertisement.getCreator()))
                .reactions(reactionMapper.convertToCountDto(advertisement.getReactions()))
                .comments(commentMapper.convertToDto(advertisement.getComments()
                        .stream()
                        .limit(2)
                        .toList()))
                .build();
    }
}
