package pl.jdacewicz.sharingservice.dto.mapper;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.PostGroupDto;
import pl.jdacewicz.sharingservice.model.PostGroup;

@Component
public class PostGroupMapper {

    private final PostMapper postMapper;
    private final UserMapper userMapper;

    @Autowired
    public PostGroupMapper(PostMapper postMapper, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }

    public PostGroupDto convertToDto(PostGroup group) {
        return PostGroupDto.builder()
                .name(EmojiParser.parseToUnicode(group.getName()))
                .imagePath(group.getImagePath())
                .creator(userMapper.convertToDto(group.getCreator()))
                .posts(postMapper.convertToDto(group.getPosts()))
                .build();
    }
}
