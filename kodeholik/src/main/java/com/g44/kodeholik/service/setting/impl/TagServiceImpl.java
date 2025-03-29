package com.g44.kodeholik.service.setting.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.g44.kodeholik.model.dto.response.setting.TagResponseDto;
import com.g44.kodeholik.model.dto.response.setting.TopicResponseDto;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.exception.NotFoundException;
import com.g44.kodeholik.model.dto.request.setting.AddTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.EditTagRequestDto;
import com.g44.kodeholik.model.dto.request.setting.FilterTagRequestDto;
import com.g44.kodeholik.model.entity.setting.Language;
import com.g44.kodeholik.model.entity.setting.Skill;
import com.g44.kodeholik.model.entity.setting.Topic;
import com.g44.kodeholik.model.enums.setting.TagType;
import com.g44.kodeholik.repository.setting.LanguageRepository;
import com.g44.kodeholik.repository.setting.SkillRepository;
import com.g44.kodeholik.repository.setting.TopicRepository;
import com.g44.kodeholik.service.setting.TagService;
import com.g44.kodeholik.service.user.UserService;
import com.g44.kodeholik.util.mapper.response.tag.TagResponseMapperLanguage;
import com.g44.kodeholik.util.mapper.response.tag.TagResponseMapperSkill;
import com.g44.kodeholik.util.mapper.response.tag.TagResponseMapperTopic;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final SkillRepository skillRepository;

    private final TopicRepository topicRepository;

    private final LanguageRepository languageRepository;

    private final UserService userService;

    private final TagResponseMapperLanguage tagResponseMapperLanguage;

    private final TagResponseMapperSkill tagResponseMapperSkill;

    private final TagResponseMapperTopic tagResponseMapperTopic;

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
        if (languageRepository.findByName(addTagRequestDto.getName()).isPresent()) {
            throw new BadRequestException("Language already exists", "Language already exists");
        }
        language.setName(addTagRequestDto.getName());
        language.setCreatedAt(Timestamp.from(Instant.now().plusMillis(25200000)));
        language.setCreatedBy(userService.getCurrentUser());
        languageRepository.save(language);
    }

    private void addTopic(AddTagRequestDto addTagRequestDto) {
        Topic topic = new Topic();
        if (topicRepository.findByName(addTagRequestDto.getName()).isPresent()) {
            throw new BadRequestException("Topic already exists", "Topic already exists");
        }
        topic.setName(addTagRequestDto.getName());
        topic.setCreatedAt(Timestamp.from(Instant.now().plusMillis(25200000)));
        topic.setCreatedBy(userService.getCurrentUser());
        topicRepository.save(topic);
    }

    private void addSkill(AddTagRequestDto addTagRequestDto) {
        Skill skill = new Skill();
        if (skillRepository.findByName(addTagRequestDto.getName()).isPresent()) {
            throw new BadRequestException("Skill already exists", "Skill already exists");
        }
        skill.setName(addTagRequestDto.getName());
        skill.setLevel(addTagRequestDto.getLevel());
        skill.setCreatedAt(Timestamp.from(Instant.now().plusMillis(25200000)));
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

    private void editSkill(Long id, EditTagRequestDto editTagRequestDto) {
        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found", "Skill not found"));
        if (!skill.getName().equals(editTagRequestDto.getName())) {
            if (skillRepository.findByName(editTagRequestDto.getName()).isPresent()) {
                throw new BadRequestException("Skill already exists", "Skill already exists");
            }
        }
        skill.setName(editTagRequestDto.getName());
        skill.setLevel(editTagRequestDto.getLevel());
        skill.setUpdatedAt(Timestamp.from(Instant.now().plusMillis(25200000)));
        skill.setUpdatedBy(userService.getCurrentUser());
        skillRepository.save(skill);
    }

    private void editTopic(Long id, EditTagRequestDto editTagRequestDto) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Topic not found", "Topic not found"));
        if (!topic.getName().equals(editTagRequestDto.getName())) {
            if (topicRepository.findByName(editTagRequestDto.getName()).isPresent()) {
                throw new BadRequestException("Topic already exists", "Topic already exists");
            }
        }
        topic.setName(editTagRequestDto.getName());
        topic.setUpdatedAt(Timestamp.from(Instant.now().plusMillis(25200000)));
        topic.setUpdatedBy(userService.getCurrentUser());
        topicRepository.save(topic);
    }

    private void editLanguage(Long id, EditTagRequestDto editTagRequestDto) {
        Language language = languageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Language not found", "Language not found"));
        if (!language.getName().equals(editTagRequestDto.getName())) {
            if (languageRepository.findByName(editTagRequestDto.getName()).isPresent()) {
                throw new BadRequestException("Language already exists", "Language already exists");
            }
        }
        language.setName(editTagRequestDto.getName());
        language.setUpdatedAt(Timestamp.from(Instant.now().plusMillis(25200000)));
        language.setUpdatedBy(userService.getCurrentUser());
        languageRepository.save(language);
    }

    @Override
    public void editTag(Long id, EditTagRequestDto editTagRequestDto) {
        if (editTagRequestDto.getType() == TagType.LANGUAGE) {
            editLanguage(id, editTagRequestDto);
        } else if (editTagRequestDto.getType() == TagType.TOPIC) {
            editTopic(id, editTagRequestDto);
        } else {
            editSkill(id, editTagRequestDto);
        }
    }

    @Override
    public void deleteTag(Long id, TagType type) {
        try {
            if (type == TagType.LANGUAGE) {
                languageRepository.deleteById(id);
            } else if (type == TagType.TOPIC) {
                topicRepository.deleteById(id);
            } else {
                skillRepository.deleteById(id);
            }
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("Cannot delete this tag because it has other object references to it",
                    "Cannot delete this tag because it has other object references to it");
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

    @Override
    public List<TopicResponseDto> getAllTopicsIdAndName() {
        List<Topic> topics = topicRepository.findAll();
        return topics.stream()
                .map(topic -> new TopicResponseDto(topic.getId(), topic.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TagResponseDto> getListTag(FilterTagRequestDto filterTagRequestDto) {
        int page = filterTagRequestDto.getPage();
        Integer size = filterTagRequestDto.getSize();
        Boolean ascending = filterTagRequestDto.getAscending();
        Sort sort = ascending != null && ascending
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page, size == null ? 5 : size.intValue(), sort);

        Page<TagResponseDto> result;
        if (filterTagRequestDto.getType() == TagType.LANGUAGE) {
            Page<Language> languages = languageRepository.findByNameContains(filterTagRequestDto.getName(), pageable);
            result = languages.map(tagResponseMapperLanguage::mapFrom);
        } else if (filterTagRequestDto.getType() == TagType.TOPIC) {
            Page<Topic> topics = topicRepository.findByNameContains(filterTagRequestDto.getName(), pageable);
            result = topics.map(tagResponseMapperTopic::mapFrom);
        } else {
            Page<Skill> skills = skillRepository.findByNameContains(filterTagRequestDto.getName(), pageable);
            result = skills.map(tagResponseMapperSkill::mapFrom);
        }

        return result;
    }
}
