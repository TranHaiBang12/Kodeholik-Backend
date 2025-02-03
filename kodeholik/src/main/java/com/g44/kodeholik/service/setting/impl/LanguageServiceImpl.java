package com.g44.kodeholik.service.setting.impl;

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

}
