package com.g44.kodeholik.repository.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemSubmission;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.g44.kodeholik.repository.setting.LanguageRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ProblemSubmissionRepositoryTest {

    @Autowired
    private ProblemSubmissionRepository underTest;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void testCountByIsAcceptedAndProblem() {
        Users user = new Users();
        user.setId(1L);

        Language language = new Language();
        language.setName("J");
        language.setCreatedAt(Timestamp.from(Instant.now()));
        language.setCreatedBy(user);

        // add language
        language = languageRepository.save(language);

        Problem problem = new Problem("test", "", Difficulty.EASY, 0, 0, ProblemStatus.PUBLIC,
                Timestamp.from(Instant.now()), user);
        problem = problemRepository.save(problem);

        ProblemSubmission problemSubmission = new ProblemSubmission(problem, user, "test", language, true, 0, 0,
                Timestamp.from(Instant.now()), "test", "test");
        underTest.save(problemSubmission);

        assertEquals(1, underTest.countByIsAcceptedAndProblem(true, problem));
        assertEquals(0, underTest.countByIsAcceptedAndProblem(false, problem));

        underTest.delete(problemSubmission);
        problemRepository.delete(problem);
        languageRepository.delete(language);
    }

    @Test
    void testCountByUserAndIsAcceptedAndProblemIn() {
        Users user = new Users();
        user.setId(1L);

        Language language = new Language();
        language.setName("J");
        language.setCreatedAt(Timestamp.from(Instant.now()));
        language.setCreatedBy(user);

        // add language
        language = languageRepository.save(language);

        Problem problem = new Problem("test", "", Difficulty.EASY, 0, 0, ProblemStatus.PUBLIC,
                Timestamp.from(Instant.now()), user);
        problem = problemRepository.save(problem);
        List<Problem> problemList = new ArrayList();
        problemList.add(problem);

        ProblemSubmission problemSubmission = new ProblemSubmission(problem, user, "test", language, true, 0, 0,
                Timestamp.from(Instant.now()), "test", "test");
        problemSubmission = underTest.save(problemSubmission);

        long count = underTest.countByUserAndIsAcceptedAndProblemIn(user, true, problemList);

        assertEquals(1, count);

        underTest.delete(problemSubmission);
        problemRepository.delete(problem);
        languageRepository.delete(language);
    }
}
