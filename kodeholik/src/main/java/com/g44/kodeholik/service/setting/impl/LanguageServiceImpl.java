package com.g44.kodeholik.service.setting.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.repository.setting.LanguageRepository;
import com.g44.kodeholik.service.setting.LanguageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {
    private final LanguageRepository languageRepository;

    @Override
    public Language findByName(String name) {
        return languageRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Language name not found", "Language name not found"));
    }

    @Override
    public Set<Language> getLanguagesByNameList(List<String> names) {
        return languageRepository.findByNameIn(names);
    }

    @Override
    public Language findById(Long id) {
        return languageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Language id not found", "Language id not found"));
    }

    @Override
    public List<String> getLanguageNamesByList(Set<Language> languages) {
        List<String> result = new ArrayList<>();
        for (Language language : languages) {
            result.add(language.getName());
        }
        return result;
    }

}
