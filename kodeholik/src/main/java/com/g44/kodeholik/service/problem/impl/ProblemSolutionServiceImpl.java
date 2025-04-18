package com.g44.kodeholik.service.problem.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.ForbiddenException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.problem.add.ShareSolutionRequestDto;
import com.g44.kodeholik.model.dto.response.problem.solution.ProblemSolutionDto;
import com.g44.kodeholik.model.dto.response.problem.solution.SolutionListResponseDto;
import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSolution;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.problem.SolutionCode;
import com.g44.kodeholik.model.entity.problem.SolutionLanguageId;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.repository.problem.ProblemSolutionRepository;
import com.g44.kodeholik.repository.setting.SkillRepository;
import com.g44.kodeholik.service.problem.ProblemSolutionService;
import com.g44.kodeholik.service.setting.TagService;
import com.g44.kodeholik.util.mapper.request.exam.AddExamRequestMapper;
import com.g44.kodeholik.util.mapper.request.user.AddUserAvatarFileMapper;
import com.g44.kodeholik.util.mapper.response.problem.ProblemSolutionMapper;
import com.g44.kodeholik.util.mapper.response.problem.SolutionListResponseMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProblemSolutionServiceImpl implements ProblemSolutionService {

    private final ProblemSolutionRepository problemSolutionRepository;

    private final ProblemSolutionMapper problemSolutionMapper;

    private final SolutionListResponseMapper solutionListResponseMapper;

    private final TagService tagService;

    @Override
    public ProblemSolution save(ProblemSolution problemSolution) {
        return problemSolutionRepository.save(problemSolution);
    }

    @Override
    public List<ProblemSolution> saveAll(List<ProblemSolution> problemSolutions) {
        return problemSolutionRepository.saveAll(problemSolutions);
    }

    @Override
    public List<ProblemSolution> findEditorialByProblem(Problem problem) {
        return problemSolutionRepository.findByProblemAndIsProblemImplementation(problem, true);
    }

    @Transactional
    @Override
    public void deleteEditorialByProblem(Problem problem) {
        problemSolutionRepository.deleteAllByProblemAndIsProblemImplementation(problem, true);
    }

    @Override
    public Page<ProblemSolutionDto> findListSolutionByProblem(Problem problem, Pageable pageable) {
        Page<ProblemSolution> solution = problemSolutionRepository.findByProblem(problem, pageable);
        return solution.map(problemSolutionMapper::mapFrom);
    }

    @Override
    public ProblemSolutionDto findSolutionDtoById(Long id) {
        ProblemSolution solution = problemSolutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solution not found", "Solution not found"));
        ProblemSolutionDto problemSolutionDto = problemSolutionMapper.mapFrom(solution);

        return problemSolutionDto;
    }

    @Override
    public ProblemSolution findSolutionById(Long id) {
        ProblemSolution solution = problemSolutionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Solution not found", "Solution not found"));
        return solution;
    }

    @Override
    public Page<SolutionListResponseDto> findOtherSolutionByProblem(Problem problem, int page, Integer size,
            String title,
            Set<Skill> skillList,
            Language language,
            String sortBy, Boolean ascending, Pageable pageable, Users currentUser) {
        int sizeN = 5;
        if (title != null)
            title = title.trim();
        if (size != null) {
            sizeN = size;
        }
        pageable = PageRequest.of(page, sizeN);
        if (sortBy != null) {
            Sort sort;
            switch (sortBy) {
                case "DATE":
                    sort = ascending != null && ascending.booleanValue() ? Sort.by("createdAt").ascending()
                            : Sort.by("createdAt").descending();
                    pageable = PageRequest.of(page, sizeN, sort);
                    break;
                case "VOTE":
                    sort = ascending != null && ascending.booleanValue() ? Sort.by("noUpvote").ascending()
                            : Sort.by("noUpvote").descending();
                    pageable = PageRequest.of(page, sizeN, sort);
                    break;
                case "COMMENT":
                    sort = ascending != null && ascending.booleanValue() ? Sort.by("noComment").ascending()
                            : Sort.by("noComment").descending();
                    pageable = PageRequest.of(page, sizeN, sort);
                    break;
                default:
                    throw new BadRequestException("Sort field is wrong", "Sort field is wrong");
            }
        }
        Page<ProblemSolution> solution;
        if (skillList != null && !skillList.isEmpty()) {
            solution = problemSolutionRepository
                    .findByProblemAndIsProblemImplementationAndTitleContainAndSkillsIn(
                            problem,
                            title,
                            language,
                            false,
                            skillList,
                            pageable);
        } else {
            solution = problemSolutionRepository
                    .findByProblemAndIsProblemImplementationAndTitleContain(
                            problem,
                            title,
                            language,
                            false,
                            pageable);
        }
        solutionListResponseMapper.currentUser = currentUser;
        Page<SolutionListResponseDto> solutionPage = solution.map(solutionListResponseMapper::mapFrom);
        for (SolutionListResponseDto solutionListResponseDto : solutionPage) {
            solutionListResponseDto.setCurrentUserCreated(
                    currentUser.getId().longValue() == solutionListResponseDto.getCreatedBy().getId());

        }
        return solutionPage;
    }

    @Override
    public void upvoteSolution(Long solutionId, Users user) {
        ProblemSolution problemSolution = findSolutionById(solutionId);
        Set<Users> usersVote = problemSolution.getUserVote();
        for (Users userVote : usersVote) {
            if (userVote.getEmail().equals(user.getEmail())) {
                throw new BadRequestException("You have already voted this solution",
                        "You have already voted this solution");
            }
        }
        usersVote.add(user);
        problemSolution.setNoUpvote(problemSolution.getNoUpvote() + 1);
        problemSolutionRepository.save(problemSolution);
    }

    @Override
    public void unupvoteSolution(Long solutionId, Users user) {
        ProblemSolution problemSolution = findSolutionById(solutionId);
        Set<Users> usersVote = problemSolution.getUserVote();

        boolean isVote = false;
        Iterator<Users> iterator = usersVote.iterator();
        while (iterator.hasNext()) {
            Users userVote = iterator.next();
            if (userVote.getEmail().equals(user.getEmail())) {
                isVote = true;
                iterator.remove(); // Xóa phần tử một cách an toàn
                if (problemSolution.getNoUpvote() > 0) {
                    problemSolution.setNoUpvote(problemSolution.getNoUpvote() - 1);
                }
                break; // Không cần tiếp tục vòng lặp
            }
        }

        if (!isVote) {
            throw new BadRequestException("You haven't voted this solution",
                    "You haven't voted this solution");
        }

        problemSolutionRepository.save(problemSolution); // Chỉ lưu một lần sau khi xử lý
    }

    @Override
    public ProblemSolutionDto postSolution(ShareSolutionRequestDto shareSolutionRequestDto, Users user) {

        if (shareSolutionRequestDto.getLink() == null) {
            throw new BadRequestException("Link cannot be null", "Link cannot be null");
        }
        if (shareSolutionRequestDto.getSubmissionId() == null || shareSolutionRequestDto.getSubmissionId().isEmpty()) {
            throw new BadRequestException("Submission cannot be empty", "Submission cannot be empty");
        }

        Problem problem = shareSolutionRequestDto.getProblem();
        String title = shareSolutionRequestDto.getTitle();
        String textSolution = shareSolutionRequestDto.getTextSolution();
        int noUpvote = 0;
        int noComment = 0;
        ProblemSolution problemSolution = ProblemSolution.builder()
                .problem(problem)
                .title(title)
                .textSolution(textSolution)
                .isProblemImplementation(false)
                .noUpvote(noUpvote)
                .noComment(noComment)
                .createdBy(user)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
        Set<Skill> skills = tagService.getSkillsByNameList(shareSolutionRequestDto.getSkills());

        problemSolution.setSkills(skills);
        Set<SolutionCode> solutionCodes = new HashSet();
        List<String> codeList = new ArrayList();
        List<ProblemSubmission> problemSubmissionList = shareSolutionRequestDto.getSubmissions();
        for (int j = 0; j < problemSubmissionList.size(); j++) {
            ProblemSubmission problemSubmission = problemSubmissionList.get(j);
            if (!problemSubmission.isAccepted()) {
                throw new BadRequestException(
                        "You can't post a solution that has not been accepted yet",
                        "You can't post a solution that has not been accepted yet");
            }
            SolutionCode solutionCode = new SolutionCode();
            String code = problemSubmission.getCode();
            if (!codeList.contains(code)) {
                solutionCode.setCode(code);
                codeList.add(code);
            } else {
                throw new BadRequestException(
                        "There are duplicate code in the solution you trying to post. Please try again",
                        "There are duplicate code in the solution you trying to post. Please try again");
            }
            SolutionLanguageId solutionLanguageId = new SolutionLanguageId();
            solutionLanguageId.setLanguageId(problemSubmission.getLanguage().getId());
            solutionLanguageId.setSolutionId(problemSolution.getId());

            solutionCode.setId(solutionLanguageId);
            solutionCode.setLanguage(problemSubmission.getLanguage());
            solutionCode.setProblem(problemSubmission.getProblem());
            solutionCode.setSolution(problemSolution);
            solutionCode.setProblemSubmission(problemSubmission);
            solutionCodes.add(solutionCode);
        }
        problemSolution.setSolutionCodes(solutionCodes);
        problemSolutionRepository.save(problemSolution);
        return problemSolutionMapper.mapFrom(problemSolution);

    }

    @Override
    public ProblemSolutionDto editSolution(ShareSolutionRequestDto shareSolutionRequestDto, Users user, Long solutionId,
            Set<Skill> skills) {
        ProblemSolution problemSolution = findSolutionById(solutionId);
        if (!problemSolution.getCreatedBy().getEmail().equals(user.getEmail())) {
            throw new ForbiddenException("You are not the owner of this solution",
                    "You are not the owner of this solution");
        }
        if (shareSolutionRequestDto.getSubmissionId() == null || shareSolutionRequestDto.getSubmissionId().isEmpty()) {
            throw new BadRequestException("Submission cannot be empty", "Submission cannot be empty");
        }
        problemSolution.setTitle(shareSolutionRequestDto.getTitle());
        problemSolution.setTextSolution(shareSolutionRequestDto.getTextSolution());
        problemSolution.setSkills(skills);
        problemSolution.setUpdatedAt(Timestamp.from(Instant.now()));
        problemSolution.setUpdatedBy(user);

        Set<SolutionCode> solutionCodes = new HashSet();
        List<String> codeList = new ArrayList();
        List<ProblemSubmission> problemSubmissionList = shareSolutionRequestDto.getSubmissions();
        for (int j = 0; j < problemSubmissionList.size(); j++) {
            ProblemSubmission problemSubmission = problemSubmissionList.get(j);
            if (!problemSubmission.isAccepted()) {
                throw new BadRequestException(
                        "You can't post a solution that has not been accepted yet",
                        "You can't post a solution that has not been accepted yet");
            }
            SolutionCode solutionCode = new SolutionCode();
            String code = problemSubmission.getCode();
            for (SolutionCode stc : solutionCodes) {
                if (stc.getLanguage().equals(problemSubmission.getLanguage())) {
                    throw new BadRequestException(
                            "At each solution, you can only post 1 language",
                            "At each solution, you can only post 1 language");
                }
            }
            if (!codeList.contains(code)) {
                solutionCode.setCode(code);
                codeList.add(code);
            } else {
                throw new BadRequestException(
                        "There are duplicate code in the solution you trying to post. Please try again",
                        "There are duplicate code in the solution you trying to post. Please try again");
            }
            SolutionLanguageId solutionLanguageId = new SolutionLanguageId();
            solutionLanguageId.setLanguageId(problemSubmission.getLanguage().getId());
            solutionLanguageId.setSolutionId(problemSolution.getId());

            solutionCode.setId(solutionLanguageId);
            solutionCode.setLanguage(problemSubmission.getLanguage());
            solutionCode.setProblem(problemSubmission.getProblem());
            solutionCode.setSolution(problemSolution);
            solutionCode.setProblemSubmission(problemSubmission);
            solutionCodes.add(solutionCode);
        }
        Set<SolutionCode> solutionCodeList = problemSolution.getSolutionCodes();
        solutionCodeList.retainAll(solutionCodes);
        solutionCodeList.addAll(solutionCodes);
        problemSolution.setSolutionCodes(solutionCodeList);

        problemSolutionRepository.save(problemSolution);
        return problemSolutionMapper.mapFrom(problemSolution);

    }

}
