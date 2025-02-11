package com.g44.kodeholik.repository.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemInputParameter;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

@SpringBootTest
@ActiveProfiles("test")
public class ProblemInputParameterRepositoryTest {

    @Autowired
    private ProblemInputParameterRepository underTest;

    @Autowired
    private ProblemRepository problemRepository;

    @Test
    void testFindByProblem() {
        Users user = new Users();
        user.setId(1L);

        Problem problem = new Problem("test", "", Difficulty.EASY, 0, 0, ProblemStatus.PUBLIC,
                Timestamp.from(Instant.now()), user);
        problem = problemRepository.save(problem);

        List<ProblemInputParameter> inputParameters = new ArrayList<ProblemInputParameter>();
        ProblemInputParameter problemInputParameter1 = new ProblemInputParameter(problem, "arr", InputType.ARR_INT);
        ProblemInputParameter problemInputParameter2 = new ProblemInputParameter(problem, "nums", InputType.INT);
        inputParameters.add(problemInputParameter1);
        inputParameters.add(problemInputParameter2);

        underTest.saveAll(inputParameters);

        List<ProblemInputParameter> savedList = underTest.findByProblem(problem);
        assertTrue(savedList.size() == 2);
        for (int i = 0; i < savedList.size(); i++) {
            if (i == 0) {
                assertEquals("arr", savedList.get(i).getName());
                assertEquals(InputType.ARR_INT, savedList.get(i).getType());
            } else {
                assertEquals("nums", savedList.get(i).getName());
                assertEquals(InputType.INT, savedList.get(i).getType());
            }
        }

        underTest.deleteAll(inputParameters);
        problemRepository.delete(problem);
    }

    @Test
    void testFindByProblemNotExisted() {
        Users user = new Users();
        user.setId(1L);
        // Problem problem, String name, InputType type
        Problem problem = new Problem("test", "", Difficulty.EASY, 0, 0, ProblemStatus.PUBLIC,
                Timestamp.from(Instant.now()), user);
        problem = problemRepository.save(problem);

        List<ProblemInputParameter> savedList = underTest.findByProblem(problem);
        assertTrue(savedList.isEmpty());

        problemRepository.delete(problem);
    }
}
