package com.g44.kodeholik.repository.setting;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.setting.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    public Optional<Language> findByName(String name);

    public Set<Language> findByNameIn(List<String> names);
}
