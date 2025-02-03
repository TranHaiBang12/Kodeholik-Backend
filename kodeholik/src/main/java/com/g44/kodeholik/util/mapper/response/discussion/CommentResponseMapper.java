package com.g44.kodeholik.util.mapper.response.discussion;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentResponseMapper implements Mapper<Comment, CommentResponseDto> {

    private final ModelMapper modelMapper;

    @Override
    public Comment mapTo(CommentResponseDto b) {
        return modelMapper.map(b, Comment.class);
    }

    @Override
    public CommentResponseDto mapFrom(Comment a) {
        return modelMapper.map(a, CommentResponseDto.class);
    }

}
