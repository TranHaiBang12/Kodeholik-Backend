package com.g44.kodeholik.service.problem;

import java.util.List;

import org.springframework.data.domain.Page;

import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.search.SearchProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.search.ProblemSortField;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.problem.Problem;

public interface ProblemService {
    public List<ProblemResponseDto> getAllProblems();

    public ProblemResponseDto getProblemResponseDtoById(Long id);

    public Problem getProblemById(Long id);

    public ProblemDescriptionResponseDto getProblemDescriptionById(Long id);

    public ProblemResponseDto createProblem(ProblemRequestDto problemRequest);

    public ProblemResponseDto updateProblem(Long id, ProblemRequestDto problemRequest);

    public void deleteProblem(Long id);

    public Page<ProblemElasticsearch> searchProblems(SearchProblemRequestDto searchProblemRequestDto, Integer page,
            Integer size, ProblemSortField sortBy, Boolean ascending);

    public List<String> getAutocompleteSuggestionsForProblemTitle(String searchText);
}
