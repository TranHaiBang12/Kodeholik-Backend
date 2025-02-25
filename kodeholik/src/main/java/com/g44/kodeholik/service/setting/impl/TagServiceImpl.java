package com.g44.kodeholik.service.setting.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.setting.AddTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.EditTagRequestDto;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.repository.setting.LanguageRepository;
import com.g44.kodeholik.repository.setting.SkillRepository;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.service.setting.TagService;
import com.g44.kodeholik.service.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final SkillRepository skillRepository;

    private final TopicRepository topicRepository;

    private final LanguageRepository languageRepository;

    private final UserService userService;

    @Override
    public Set<Skill> getSkillsByNameList(List<String> names) {
        return skillRepository.findByNameIn(names);
    }

    @Override
    public Set<Topic> getTopicsByNameList(List<String> names) {
        return topicRepository.findByNameIn(names);
    }

    private void addLanguage(AddTagRequestDto addTagRequestDto) {
        Language language = new Language();
        language.setName(addTagRequestDto.getName());
        language.setCreatedAt(Timestamp.from(Instant.now()));
        language.setCreatedBy(userService.getCurrentUser());
        languageRepository.save(language);
    }

    private void addTopic(AddTagRequestDto addTagRequestDto) {
        Topic topic = new Topic();
        topic.setName(addTagRequestDto.getName());
        topic.setCreatedAt(Timestamp.from(Instant.now()));
        topic.setCreatedBy(userService.getCurrentUser());
        topicRepository.save(topic);
    }

    private void addSkill(AddTagRequestDto addTagRequestDto) {
        Skill skill = new Skill();
        skill.setName(addTagRequestDto.getName());
        skill.setLevel(addTagRequestDto.getLevel());
        skill.setCreatedAt(Timestamp.from(Instant.now()));
        skill.setCreatedBy(userService.getCurrentUser());
        skillRepository.save(skill);
    }

    @Override
    public void addTag(AddTagRequestDto addTagRequestDto) {
        if (addTagRequestDto.getType() == TagType.LANGUAGE) {
            addLanguage(addTagRequestDto);
        } else if (addTagRequestDto.getType() == TagType.TOPIC) {
            addTopic(addTagRequestDto);
        } else {
            addSkill(addTagRequestDto);
        }
    }

    private void editSkill(EditTagRequestDto editTagRequestDto) {
        Skill skill = skillRepository.findById(editTagRequestDto.getId())
                .orElseThrow(() -> new NotFoundException("Skill not found", "Skill not found"));
        skill.setName(editTagRequestDto.getName());
        skill.setLevel(editTagRequestDto.getLevel());
        skill.setUpdatedAt(Timestamp.from(Instant.now()));
        skill.setUpdatedBy(userService.getCurrentUser());
        skillRepository.save(skill);
    }

    private void editTopic(EditTagRequestDto editTagRequestDto) {
        Topic topic = topicRepository.findById(editTagRequestDto.getId())
                .orElseThrow(() -> new NotFoundException("Topic not found", "Topic not found"));
        topic.setName(editTagRequestDto.getName());
        topic.setUpdatedAt(Timestamp.from(Instant.now()));
        topic.setUpdatedBy(userService.getCurrentUser());
        topicRepository.save(topic);
    }

    private void editLanguage(EditTagRequestDto editTagRequestDto) {
        Language language = languageRepository.findById(editTagRequestDto.getId())
                .orElseThrow(() -> new NotFoundException("Language not found", "Language not found"));
        language.setName(editTagRequestDto.getName());
        language.setUpdatedAt(Timestamp.from(Instant.now()));
        language.setUpdatedBy(userService.getCurrentUser());
        languageRepository.save(language);
    }

    @Override
    public void editTag(EditTagRequestDto editTagRequestDto) {
        if (editTagRequestDto.getType() == TagType.LANGUAGE) {
            editLanguage(editTagRequestDto);
        } else if (editTagRequestDto.getType() == TagType.TOPIC) {
            editTopic(editTagRequestDto);
        } else {
            editSkill(editTagRequestDto);
        }
    }

    @Override
    public void deleteTag(Long id, TagType type) {
        if (type == TagType.LANGUAGE) {
            languageRepository.deleteById(id);
        } else if (type == TagType.TOPIC) {
            topicRepository.deleteById(id);
        } else {
            skillRepository.deleteById(id);
        }
    }

    @Override
    public List<String> getAllSkills() {
        List<String> skillList = new ArrayList();
        List<Skill> skills = skillRepository.findAll();
        for (Skill skill : skills) {
            skillList.add(skill.getName());
        }
        return skillList;
    }

    @Override
    public List<String> getAllTopics() {
        List<String> topicList = new ArrayList();
        List<Topic> topics = topicRepository.findAll();
        for (Topic topic : topics) {
            topicList.add(topic.getName());
        }
        return topicList;
    }
}
