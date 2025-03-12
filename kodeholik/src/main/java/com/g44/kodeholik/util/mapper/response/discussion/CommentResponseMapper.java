package com.g44.kodeholik.util.mapper.response.discussion;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.g44.kodeholik.model.dto.response.discussion.CommentResponseDto;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.service.aws.s3.S3Service;
import com.g44.kodeholik.util.mapper.Mapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentResponseMapper implements Mapper<Comment, CommentResponseDto> {

    private final ModelMapper modelMapper;

    private final S3Service s3Service;

    @Override
    public Comment mapTo(CommentResponseDto b) {
        return modelMapper.map(b, Comment.class);
    }

    @Override
    public CommentResponseDto mapFrom(Comment a) {
        CommentResponseDto commentResponseDto = modelMapper.map(a, CommentResponseDto.class);
        if (a.getCommentReply() != null) {
            commentResponseDto.setReplyId(a.getCommentReply().getId());
        } else {
            commentResponseDto.setReplyId(null);
        }
        if (commentResponseDto.getCreatedBy() != null) {
            UserResponseDto userResponseDto = commentResponseDto.getCreatedBy();
            if (userResponseDto.getAvatar() != null && userResponseDto.getAvatar().startsWith("kodeholik")) {
                userResponseDto.setAvatar(s3Service.getPresignedUrl(userResponseDto.getAvatar()));
            }
        }
        return commentResponseDto;
    }

}
