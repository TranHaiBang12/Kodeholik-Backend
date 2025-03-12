package com.g44.kodeholik.util.mapper.response.tag;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.setting.TagResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagResponseMapperLanguage implements Mapper<Language, TagResponseDto> {

    private final ModelMapper modelMapper;

    private final S3Service s3Service;

    @Override
    public Language mapTo(TagResponseDto b) {
        return modelMapper.map(b, Language.class);
    }

    @Override
    public TagResponseDto mapFrom(Language a) {
        TagResponseDto tagResponseDto = modelMapper.map(a, TagResponseDto.class);
        tagResponseDto.setType(TagType.LANGUAGE);
        updateAvatar(tagResponseDto.getCreatedBy());
        return tagResponseDto;
    }

    private void updateAvatar(UserResponseDto user) {
        if (user != null && user.getAvatar() != null && user.getAvatar().startsWith("kodeholik")) {
            user.setAvatar(s3Service.getPresignedUrl(user.getAvatar()));
        }
    }

}
