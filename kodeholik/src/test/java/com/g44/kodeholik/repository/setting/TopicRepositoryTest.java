package com.g44.kodeholik.repository.setting;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.entity.user.Users;

@SpringBootTest
@ActiveProfiles("test")
public class TopicRepositoryTest {

    @Autowired
    private TopicRepository underTest;

    @Test
    void testFindByNameIn() {
        Users user = new Users();
        user.setId(1L);

        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("A", Timestamp.from(Instant.now()), user));
        topics.add(new Topic("B", Timestamp.from(Instant.now()), user));
        underTest.saveAll(topics);

        List<String> names = List.of("A", "B");
        Set<Topic> foundTopics = underTest.findByNameIn(names);

        assertEquals(foundTopics.size(), 2);

        underTest.deleteAll(foundTopics);
    }

    @Test
    void testFindByNameNotIn() {
        Users user = new Users();
        user.setId(1L);

        List<Topic> topics = new ArrayList<>();
        topics.add(new Topic("A", Timestamp.from(Instant.now()), user));
        topics.add(new Topic("B", Timestamp.from(Instant.now()), user));
        underTest.saveAll(topics);

        List<String> names = List.of("A", "B");
        Set<Topic> foundTopics = underTest.findByNameIn(names);

        assertEquals(foundTopics.size(), 2);

        underTest.deleteAll(foundTopics);
    }
}
