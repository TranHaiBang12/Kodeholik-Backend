package com.g44.kodeholik.service.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.g44.kodeholik.model.dto.request.lambda.TestCase;
import com.g44.kodeholik.model.dto.request.problem.ProblemCompileRequestDto;
import com.g44.kodeholik.model.dto.request.problem.ProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemBasicAddDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemEditorialDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemInputParameterDto;
import com.g44.kodeholik.model.dto.request.problem.add.ProblemTestCaseDto;
import com.g44.kodeholik.model.dto.request.problem.search.SearchProblemRequestDto;
import com.g44.kodeholik.model.dto.request.problem.search.ProblemSortField;
import com.g44.kodeholik.model.dto.response.problem.NoAchivedInformationResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;

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

        public SubmissionResponseDto submitProblem(Long problemId, ProblemCompileRequestDto problemCompileRequestDto);

        public RunProblemResponseDto run(Long problemId, ProblemCompileRequestDto problemCompileRequestDto);

        public ProblemTemplate findByProblemAndLanguage(Long problemId, String languageName);

        public List<TestCase> getTestCaseByProblem(Long problemId);

        public List<TestCase> getSampleTestCaseByProblem(Long problemId);

        public ProblemCompileResponseDto getProblemCompileInformationById(Long problemId, String languageName);

        public List<NoAchivedInformationResponseDto> getListNoAchievedInformationByCurrentUser();

        public void addProblem(
                        ProblemBasicAddDto problemBasicAddDto,
                        ProblemEditorialDto problemEditorialDto,
                        ProblemInputParameterDto problemInputParameterDto,
                        MultipartFile excelFile);

        public void editProblem(
                        Long problemId,
                        ProblemBasicAddDto problemBasicAddDto,
                        ProblemEditorialDto problemEditorialDto,
                        ProblemInputParameterDto problemInputParameterDto,
                        MultipartFile excelFile);

        public void activateProblem(Long problemId);

        public void deactivateProblem(Long problemId);

        public boolean checkTitleExisted(String title);

}
