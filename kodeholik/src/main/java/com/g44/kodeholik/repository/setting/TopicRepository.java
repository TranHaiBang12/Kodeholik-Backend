package com.g44.kodeholik.repository.setting;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.setting.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Set<Topic> findByNameIn(List<String> names);
    Set<Topic> findByIdIn(Set<Long> ids);
}
