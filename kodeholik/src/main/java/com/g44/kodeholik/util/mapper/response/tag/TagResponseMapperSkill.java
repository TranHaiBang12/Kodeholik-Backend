package com.g44.kodeholik.util.mapper.response.tag;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.setting.TagResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagResponseMapperSkill implements Mapper<Skill, TagResponseDto> {

    private final ModelMapper mapper;

    private final S3Service s3Service;

    @Override
    public Skill mapTo(TagResponseDto b) {
        return mapper.map(b, Skill.class);
    }

    @Override
    public TagResponseDto mapFrom(Skill a) {
        TagResponseDto tagResponseDto = mapper.map(a, TagResponseDto.class);
        tagResponseDto.setType(TagType.SKILL);
        updateAvatar(tagResponseDto.getCreatedBy());
        return tagResponseDto;
    }

    private void updateAvatar(UserResponseDto user) {
        if (user != null && user.getAvatar() != null && user.getAvatar().startsWith("kodeholik")) {
            user.setAvatar(s3Service.getPresignedUrl(user.getAvatar()));
        }
    }

}
