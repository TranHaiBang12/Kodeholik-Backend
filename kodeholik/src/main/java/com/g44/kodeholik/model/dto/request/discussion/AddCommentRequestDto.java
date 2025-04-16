package com.g44.kodeholik.model.dto.request.discussion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddCommentRequestDto {

    @Size(min = 10, max = 5000, message = "MSG17")
    private String comment;

    private Long commentReply;

    private Long courseId;
}
