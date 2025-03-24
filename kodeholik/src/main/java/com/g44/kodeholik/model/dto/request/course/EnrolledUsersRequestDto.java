package com.g44.kodeholik.model.dto.request.course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrolledUsersRequestDto {
    private int page = 0;
    private int size = 10;
    private String sortBy = "enrolledAt";
    private String sortDir = "desc";
    private String username;
}
