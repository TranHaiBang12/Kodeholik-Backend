package com.g44.kodeholik.model.dto.request.discussion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequestDto {
    @NotBlank(message = "Nội dung bình luận không được để trống")
    @Size(max = 1000, message = "Bình luận không được vượt quá 1000 ký tự")
    private String comment;
}
