package com.g44.kodeholik.model.dto.request.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddCommentRequestDto {
    @NotBlank(message = "MSG17")
    private String comment;

    @NotNull(message = "MSG02")
    private CommentLocation location;

    @NotNull(message = "MSG02")
    private Long locationId;

    private Long commentReply;
}
