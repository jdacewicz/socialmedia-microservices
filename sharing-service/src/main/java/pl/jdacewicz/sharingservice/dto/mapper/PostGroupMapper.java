package pl.jdacewicz.sharingservice.dto.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jdacewicz.sharingservice.dto.PostGroupDto;
import pl.jdacewicz.sharingservice.dto.PostGroupRequest;
import pl.jdacewicz.sharingservice.model.PostGroup;

@Component
public class PostGroupMapper {

    private final PostMapper postMapper;

    @Autowired
    public PostGroupMapper(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public PostGroupDto convertToDto(PostGroup group) {
        return PostGroupDto.builder()
                .name(group.getName())
                .posts(postMapper.convertToDto(group.getPosts()))
                .build();
    }

    public PostGroup convertFromRequest(PostGroupRequest groupRequest) {
        return PostGroup.builder()
                .name(groupRequest.name())
                .build();
    }
}
