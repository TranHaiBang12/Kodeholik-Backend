package com.g44.kodeholik.repository.setting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.user.Users;
import com.g44.kodeholik.model.enums.setting.Level;

@SpringBootTest
@ActiveProfiles("test")
public class SkillRepositoryTest {

    @Autowired
    private SkillRepository underTest;

    @Test
    void testFindByNameIn() {
        Users user = new Users();
        user.setId(1L);

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("A", Level.INTERMEDIATE, Timestamp.from(Instant.now()), user));
        skills.add(new Skill("B", Level.INTERMEDIATE, Timestamp.from(Instant.now()), user));
        underTest.saveAll(skills);

        List<String> names = List.of("A", "B");
        Set<Skill> foundSkills = underTest.findByNameIn(names);

        assertEquals(foundSkills.size(), 2);

        underTest.deleteAll(skills);

    }

    @Test
    void testFindByNameNotIn() {
        Users user = new Users();
        user.setId(1L);

        List<Skill> skills = new ArrayList<>();
        skills.add(new Skill("A", Level.INTERMEDIATE, Timestamp.from(Instant.now()), user));
        skills.add(new Skill("B", Level.INTERMEDIATE, Timestamp.from(Instant.now()), user));
        underTest.saveAll(skills);

        List<String> names = List.of("D", "E");
        Set<Skill> foundSkills = underTest.findByNameIn(names);

        assertEquals(foundSkills.size(), 0);

        underTest.deleteAll(skills);

    }
}
