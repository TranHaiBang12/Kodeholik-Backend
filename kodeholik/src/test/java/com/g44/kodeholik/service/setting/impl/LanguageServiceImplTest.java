package com.g44.kodeholik.service.setting.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.repository.setting.LanguageRepository;

public class LanguageServiceImplTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageServiceImpl languageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindByNameSuccess() {
        Language language = new Language();
        language.setName("English");

        when(languageRepository.findByName("English")).thenReturn(Optional.of(language));

        Language result = languageService.findByName("English");
        assertEquals("English", result.getName());
    }

    @Test
    public void testFindByNameNotFound() {
        when(languageRepository.findByName("Spanish")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> languageService.findByName("Spanish"));
    }

    @Test
    public void testGetLanguagesByNameList() {
        Language language1 = new Language();
        language1.setName("English");
        Language language2 = new Language();
        language2.setName("Spanish");

        Set<Language> languages = new HashSet<>();
        languages.add(language1);
        languages.add(language2);

        when(languageRepository.findByNameIn(List.of("English", "Spanish"))).thenReturn(languages);

        Set<Language> result = languageService.getLanguagesByNameList(List.of("English", "Spanish"));
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIdSuccess() {
        Language language = new Language();
        language.setId(1L);

        when(languageRepository.findById(1L)).thenReturn(Optional.of(language));

        Language result = languageService.findById(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        when(languageRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> languageService.findById(2L));
    }

    @Test
    public void testGetLanguageNamesByList() {
        Language language1 = new Language();
        language1.setName("English");
        Language language2 = new Language();
        language2.setName("Spanish");

        Set<Language> languages = new HashSet<>();
        languages.add(language1);
        languages.add(language2);

        List<String> result = languageService.getLanguageNamesByList(languages);
        assertEquals(2, result.size());
    }
}