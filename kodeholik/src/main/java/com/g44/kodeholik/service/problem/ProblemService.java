package com.g44.kodeholik.service.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.g44.kodeholik.model.dto.response.problem.ProblemBasicResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemCompileResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemDescriptionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemEditorialResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemInputParameterResponseDto;
import com.g44.kodeholik.model.dto.response.problem.ProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.SubmissionResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.run.RunProblemResponseDto;
import com.g44.kodeholik.model.dto.response.problem.submission.submit.SubmissionListResponseDto;
import com.g44.kodeholik.model.elasticsearch.ProblemElasticsearch;
import com.g44.kodeholik.model.entity.discussion.Comment;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;

public interface ProblemService {
        public List<ProblemResponseDto> getAllProblems();

        public ProblemResponseDto getProblemResponseDtoById(String link);

        public Problem getProblemById(Long id);

        public ProblemDescriptionResponseDto getProblemDescriptionById(String link);

        public ProblemResponseDto createProblem(ProblemRequestDto problemRequest);

        public ProblemResponseDto updateProblem(String link, ProblemRequestDto problemRequest);

        public void deleteProblem(String link);

        public Page<ProblemElasticsearch> searchProblems(SearchProblemRequestDto searchProblemRequestDto, Integer page,
                        Integer size, ProblemSortField sortBy, Boolean ascending);

        public List<String> getAutocompleteSuggestionsForProblemTitle(String searchText);

        public SubmissionResponseDto submitProblem(String link, ProblemCompileRequestDto problemCompileRequestDto);

        public RunProblemResponseDto run(String link, ProblemCompileRequestDto problemCompileRequestDto);

        public ProblemTemplate findByProblemAndLanguage(String link, String languageName);

        public List<TestCase> getTestCaseByProblem(String link);

        public List<TestCase> getSampleTestCaseByProblem(String link);

        public ProblemCompileResponseDto getProblemCompileInformationById(String link, String languageName);

        public List<NoAchivedInformationResponseDto> getListNoAchievedInformationByCurrentUser();

        public void addProblem(
                        ProblemBasicAddDto problemBasicAddDto,
                        ProblemEditorialDto problemEditorialDto,
                        List<ProblemInputParameterDto> problemInputParameterDto,
                        MultipartFile excelFile);

        public void editProblem(
                        String link,
                        ProblemBasicAddDto problemBasicAddDto,
                        ProblemEditorialDto problemEditorialDto,
                        List<ProblemInputParameterDto> problemInputParameterDto,
                        MultipartFile excelFile);

        public void activateProblem(String link);

        public void deactivateProblem(String link);

        public boolean checkTitleExisted(String title);

        public ProblemBasicResponseDto getProblemBasicResponseDto(String link);

        public ProblemEditorialResponseDto getProblemEditorialDtoList(String link);

        public ProblemInputParameterResponseDto getProblemInputParameterDtoList(String link);

        public byte[] getExcelFile(String link);

        public Problem getActivePublicProblemByLink(String link);

        public Problem getProblemByLink(String link);

        public Page<SolutionListResponseDto> getProblemListSolution(String link, int page, Integer size, String title,
                        String languageName,
                        String sortBy, Boolean ascending, Pageable pageable);

        public ProblemSolutionDto getProblemSolutionDetail(Long solutionId);

        public void tagFavouriteProblem(String link);

        public void untagFavouriteProblem(String link);

        public void upvoteSolution(Long solutionId);

        public void unupvoteSolution(Long solutionId);

        public List<SubmissionListResponseDto> getSubmissionListByUserAndProblem(String link);

}
