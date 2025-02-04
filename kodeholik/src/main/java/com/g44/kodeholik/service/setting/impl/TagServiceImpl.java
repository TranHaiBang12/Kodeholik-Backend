package com.g44.kodeholik.service.setting.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.repository.setting.SkillRepository;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.service.setting.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final SkillRepository skillRepository;

    private final TopicRepository topicRepository;

    @Override
    public Set<Skill> getSkillsByNameList(List<String> names) {
        return skillRepository.findByNameIn(names);
    }

    @Override
    public Set<Topic> getTopicsByNameList(List<String> names) {
        return topicRepository.findByNameIn(names);
    }
}
