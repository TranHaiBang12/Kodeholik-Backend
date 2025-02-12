package com.g44.kodeholik.repository.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;

@SpringBootTest
@ActiveProfiles("test")
public class ProblemRepositoryTest {

    @Autowired
    private ProblemRepository underTest;

    @Test
    void testFindByDifficulty() {
        Users user = new Users();
        user.setId(1L);

        List<Problem> problemList = underTest.findByDifficulty(Difficulty.EASY);
        Problem problem = new Problem("test", "", Difficulty.EASY, 0, 0, ProblemStatus.PUBLIC,
                Timestamp.from(Instant.now()), user);
        problem = underTest.save(problem);
        List<Problem> newProblemList = underTest.findByDifficulty(Difficulty.EASY);

        assertEquals(1, newProblemList.size() - problemList.size());
        assertEquals("test", newProblemList.get(newProblemList.size() - 1).getTitle());
        underTest.delete(problem);
    }
}
