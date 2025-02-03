package com.g44.kodeholik.model.dto.response.discussion;

import java.security.Timestamp;

import com.g44.kodeholik.model.dto.response.user.UserResponseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentResponseDto {
    private String comment;

    private int noUpvote;

    private UserResponseDto createdBy;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private UserResponseDto commentReply;
}
