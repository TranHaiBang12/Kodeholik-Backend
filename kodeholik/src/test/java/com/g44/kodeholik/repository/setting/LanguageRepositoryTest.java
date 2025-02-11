package com.g44.kodeholik.repository.setting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.user.Users;

@SpringBootTest
@ActiveProfiles("test")
public class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository underTest;

    @Test
    void testFindByNameExisted() {
        Users user = new Users();
        user.setId(1L);

        Language language = new Language();
        language.setName("J");
        language.setCreatedAt(Timestamp.from(Instant.now()));
        language.setCreatedBy(user);

        // add language
        language = underTest.save(language);

        Optional<Language> optionalLanguage = underTest.findByName("J");
        assertTrue(optionalLanguage.isPresent());
        assertEquals("J", optionalLanguage.get().getName());

        underTest.delete(language);
    }

    @Test
    void testFindByNameNotExisted() {
        Users user = new Users();
        user.setId(1L);

        Language language = new Language();
        language.setName("J");
        language.setCreatedAt(Timestamp.from(Instant.now()));
        language.setCreatedBy(user);

        // add language
        language = underTest.save(language);

        Optional<Language> optionalLanguage = underTest.findByName("M");
        assertFalse(optionalLanguage.isPresent());

        underTest.delete(language);
    }
}
