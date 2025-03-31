package com.g44.kodeholik.model.dto.response.course;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.g44.kodeholik.model.dto.response.user.UserResponseDto;
import com.g44.kodeholik.model.entity.course.CourseRating;
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
public class CourseRatingResponseDto {
    private Long id;
    private Long courseId;
    private UserResponseDto user;
    private Integer rating;
    private String comment;
    @JsonSerialize(using = TimestampSerializer.class)
    private Long createdAt;
    @JsonSerialize(using = TimestampSerializer.class)
    private Long updatedAt;

    public CourseRatingResponseDto(CourseRating courseRating) {
        if (courseRating != null) {
            this.id = courseRating.getId();
            this.courseId = courseRating.getCourse().getId();
            this.user = new UserResponseDto(courseRating.getUser());
            this.rating = courseRating.getRating();
            this.comment = courseRating.getComment();
            this.createdAt = courseRating.getCreatedAt() != null ? courseRating.getCreatedAt().getTime() : null;
            this.updatedAt = courseRating.getUpdatedAt() != null ? courseRating.getUpdatedAt().getTime() : null;
        }
    }
}

