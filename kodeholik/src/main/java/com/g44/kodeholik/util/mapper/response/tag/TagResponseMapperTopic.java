package com.g44.kodeholik.util.mapper.response.tag;

import com.g44.kodeholik.model.dto.response.setting.TopicResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.setting.TagResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagResponseMapperTopic implements Mapper<Topic, TagResponseDto> {

    private final ModelMapper mapper;

    private final S3Service s3Service;

    @Override
    public Topic mapTo(TagResponseDto b) {
        return mapper.map(b, Topic.class);
    }

    @Override
    public TagResponseDto mapFrom(Topic a) {
        TagResponseDto tagResponseDto = mapper.map(a, TagResponseDto.class);
        tagResponseDto.setType(TagType.TOPIC);
        updateAvatar(tagResponseDto.getCreatedBy());
        return tagResponseDto;
    }

    public TopicResponseDto mapFromTopic(Topic a) {
        return TopicResponseDto.builder()
                .id(a.getId())
                .name(a.getName())
                .build();
    }

    private void updateAvatar(UserResponseDto user) {
        if (user != null && user.getAvatar() != null && user.getAvatar().startsWith("kodeholik")) {
            user.setAvatar(s3Service.getPresignedUrl(user.getAvatar()));
        }
    }

}
