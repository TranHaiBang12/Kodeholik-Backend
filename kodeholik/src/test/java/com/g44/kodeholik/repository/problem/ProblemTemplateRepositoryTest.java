package com.g44.kodeholik.repository.problem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.problem.Problem;
import com.g44.kodeholik.model.entity.problem.ProblemTemplate;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.problem.Difficulty;
import com.g44.kodeholik.model.enums.problem.InputType;
import com.g44.kodeholik.model.enums.problem.ProblemStatus;
import com.g44.kodeholik.repository.setting.LanguageRepository;

@SpringBootTest
@ActiveProfiles("test")
public class ProblemTemplateRepositoryTest {

    @Autowired
    private ProblemTemplateRepository problemTemplateRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Test
    void testFindByProblemAndLanguage() {
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

        ProblemTemplate problemTemplate = new ProblemTemplate(problem, language, "test", "test", InputType.INT);
        problemTemplateRepository.save(problemTemplate);

        Optional<ProblemTemplate> savedTemplate = problemTemplateRepository.findByProblemAndLanguage(problem, language);
        assertTrue(savedTemplate.isPresent());
        assertEquals("test", savedTemplate.get().getTemplateCode());
        assertEquals("test", savedTemplate.get().getFunctionSignature());
        assertEquals(InputType.INT, savedTemplate.get().getReturnType());

        problemTemplateRepository.delete(problemTemplate);
        problemRepository.delete(problem);
        languageRepository.delete(language);
    }
}
