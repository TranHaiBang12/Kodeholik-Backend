package com.g44.kodeholik.service.setting;

import java.util.List;
import java.util.Set;

import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;

public interface LanguageService {
    public Language findByName(String name);

    public Set<Language> getLanguagesByNameList(List<String> names);

    public Language findById(Long id);

    public List<String> getLanguageNamesByList(Set<Language> languages);
}
