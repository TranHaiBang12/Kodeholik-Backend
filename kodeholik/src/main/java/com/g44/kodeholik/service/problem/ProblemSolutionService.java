package com.g44.kodeholik.service.problem;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.g44.kodeholik.model.dto.request.problem.add.ShareSolutionRequestDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.user.Users;

public interface ProblemSolutionService {
        public ProblemSolution save(ProblemSolution problemSolution);

        public List<ProblemSolution> saveAll(List<ProblemSolution> problemSolutions);

        public List<ProblemSolution> findEditorialByProblem(Problem problem);

        public void deleteEditorialByProblem(Problem problem);

        public Page<ProblemSolutionDto> findListSolutionByProblem(Problem problem, Pageable pageable);

        public Page<SolutionListResponseDto> findOtherSolutionByProblem(Problem problem, int page, Integer size,
                        String title,
                        Set<Skill> skillList,
                        Language language,
                        String sortBy, Boolean ascending, Pageable pageable, Users currentUser);

        public ProblemSolutionDto findSolutionDtoById(Long id);

        public ProblemSolution findSolutionById(Long id);

        public void upvoteSolution(Long solutionId, Users user);

        public void unupvoteSolution(Long solutionId, Users user);

        public ProblemSolutionDto postSolution(ShareSolutionRequestDto shareSolutionRequestDto, Users user);

        public ProblemSolutionDto editSolution(ShareSolutionRequestDto shareSolutionRequestDto, Users user,
                        Long solutionId,
                        Set<Skill> skills);
}
