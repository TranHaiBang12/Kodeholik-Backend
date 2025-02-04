package com.g44.kodeholik.service.setting;

import java.util.List;
import java.util.Set;

import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;

public interface TagService {
    public Set<Skill> getSkillsByNameList(List<String> names);

    public Set<Topic> getTopicsByNameList(List<String> names);
}
