package com.g44.kodeholik.model.dto.request.course.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchCourseRequestDto {
    private String title;
    private List<String> topics;
}
