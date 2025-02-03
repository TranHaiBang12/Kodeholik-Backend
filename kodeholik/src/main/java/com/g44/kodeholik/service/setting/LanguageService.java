package com.g44.kodeholik.service.setting;

import com.g44.kodeholik.model.entity.setting.Language;

public interface LanguageService {
    public Language findByName(String name);
}
