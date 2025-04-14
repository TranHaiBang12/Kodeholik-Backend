package com.g44.kodeholik.repository.setting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.g44.kodeholik.model.entity.setting.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.setting.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Set<Topic> findByNameIn(List<String> names);

    Optional<Topic> findByName(String name);

    Set<Topic> findByIdIn(Set<Long> ids);

    Page<Topic> findByNameIgnoreCaseContains(String name, Pageable pageable);

    public Optional<Topic> findByNameIgnoreCase(String tagName);

    public Optional<Topic> findByNameIgnoreCaseAndIdNot(String tagName, Long id);
}
