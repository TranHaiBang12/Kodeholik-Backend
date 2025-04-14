package com.g44.kodeholik.repository.setting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.g44.kodeholik.model.entity.setting.Language;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.setting.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Set<Skill> findByNameIn(List<String> names);

    Page<Skill> findByNameContains(String name, Pageable pageable);

    Optional<Skill> findByName(String name);

    public Optional<Skill> findByNameIgnoreCase(String tagName);

    public Optional<Skill> findByNameIgnoreCaseAndIdNot(String tagName, Long id);
}
