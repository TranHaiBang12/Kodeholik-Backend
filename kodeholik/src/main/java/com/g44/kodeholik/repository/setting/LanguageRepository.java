package com.g44.kodeholik.repository.setting;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g44.kodeholik.model.entity.setting.Language;

public interface LanguageRepository extends JpaRepository<Language, Long> {
    public Optional<Language> findByName(String name);
}
