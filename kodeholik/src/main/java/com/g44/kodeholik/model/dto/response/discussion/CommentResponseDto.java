package com.g44.kodeholik.model.dto.response.discussion;

import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.UserCommentResponseDto;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.util.serializer.TimestampSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CommentResponseDto {

    private Long id;

    private String comment;

    private int noUpvote;

    private boolean isVoted;

    private UserCommentResponseDto createdBy;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;

    @JsonSerialize(using = TimestampSerializer.class)
    private Long updatedAt;

    private Long replyId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getComment();
        this.createdBy = new UserCommentResponseDto(comment.getCreatedBy());
        this.createdAt = comment.getCreatedAt().getTime();
        this.noUpvote = comment.getNoUpvote();
    }
}
